package com.cb.repository.daoImplementation;

import com.cb.model.StokBarang;
import com.cb.model.TransaksiBahanBakuCabang;
import com.cb.repository.StokBarangRepository;
import com.cb.repository.TransaksiCabangRepository;
import com.cb.repository.dao.TransactionalBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = false)
public class TransactionalBlockImpl implements TransactionalBlock {
    @Autowired
    private TransaksiCabangRepository transaksiCabangRepository;
    @Autowired
    private StokBarangRepository stokBarangRepository;

    @Override
    public TransaksiBahanBakuCabang saveTransactionBahanBaku(TransaksiBahanBakuCabang obj) {
        obj.setTotal(obj.getHarga() * obj.getQty());
        TransaksiBahanBakuCabang save = transaksiCabangRepository.save(obj);

        List<StokBarang> stokBarangExisting = stokBarangRepository.findByNamaGudangAndNamaBahanContainingIgnoreCaseOrderByTglTransaksiAscIdDescIdAsc(obj.getNamaGudang(), obj.getNamaBahan());

        Integer pengambilanBahanBaku = obj.getQty();

        if (stokBarangExisting.size() == 1) {
            StokBarang stokBarang = stokBarangExisting.get(0);
            int stokAwal = stokBarang.getStok();
            int stokAkhir = stokAwal - pengambilanBahanBaku;

            if (stokAkhir < 0) {
                throw new RuntimeException("Stok tidak mencukupi untuk transaksi ini");
            }

            stokBarang.setStok(stokAkhir);
            stokBarangRepository.save(stokBarang);
        }
        else if (stokBarangExisting.size() == 2) {
            StokBarang stokBarang1 = stokBarangExisting.get(0);
            StokBarang stokBarang2 = stokBarangExisting.get(1);

            int stokAwal1 = stokBarang1.getStok();
            int stokAwal2 = stokBarang2.getStok();

            if (stokAwal2 >= pengambilanBahanBaku) {
                stokBarang2.setStok(stokAwal2 - pengambilanBahanBaku);
                stokBarangRepository.save(stokBarang1);
            } else {
                int sisaPengambilan = pengambilanBahanBaku - stokAwal2;
                stokBarang2.setStok(0);
                stokBarangRepository.save(stokBarang2);

                stokBarang1.setStok(stokAwal1 - sisaPengambilan - stokAwal2);
                stokBarangRepository.save(stokBarang1);
            }
        } else if (stokBarangExisting.size() == 3) {
            StokBarang stokBarang1 = stokBarangExisting.get(0);
            StokBarang stokBarang2 = stokBarangExisting.get(1);
            StokBarang stokBarang3 = stokBarangExisting.get(2);

            int stokAwal1 = stokBarang1.getStok();
            int stokAwal2 = stokBarang2.getStok();
            int stokAwal3 = stokBarang3.getStok();

            if (stokAwal3 >= pengambilanBahanBaku) {
                stokBarang3.setStok(stokAwal3 - pengambilanBahanBaku);
                stokBarangRepository.save(stokBarang3);
            } else {
                int sisaPengambilan = pengambilanBahanBaku - stokAwal3;
                stokBarang3.setStok(0);
                stokBarangRepository.save(stokBarang3);

                if (stokAwal2 >= sisaPengambilan) {
                    stokBarang2.setStok(stokAwal2 - sisaPengambilan - stokAwal3);
                    stokBarangRepository.save(stokBarang2);

                    stokBarang1.setStok(stokAwal1 - pengambilanBahanBaku);
                    stokBarangRepository.save(stokBarang1);
                } else {
                    int sisaPengambilan2 = sisaPengambilan - stokAwal2;
                    stokBarang2.setStok(0);
                    stokBarangRepository.save(stokBarang2);

                    stokBarang1.setStok(stokAwal1 - sisaPengambilan2 - stokAwal3 - stokAwal2);
                    stokBarangRepository.save(stokBarang1);
                }
            }
        }

        return save;
    }
}
