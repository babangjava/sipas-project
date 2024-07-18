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

        List<StokBarang> stokBarangExisting = stokBarangRepository.getStokBarangExisting(obj.getNamaGudang(), obj.getNamaBahan());

        Integer pengambilanBahanBaku = obj.getQty();
        if (stokBarangExisting.size() == 1) {
            StokBarang stokBarang = stokBarangExisting.get(0);
            pengambilanBahanBaku = stokBarang.getStok() - pengambilanBahanBaku;

            stokBarang.setStok(pengambilanBahanBaku);
            stokBarangRepository.save(stokBarang);
        } else if (stokBarangExisting.size() == 2) {
            StokBarang stokBarang1 = stokBarangExisting.get(0);
            StokBarang stokBarang2 = stokBarangExisting.get(1);
            int stokAwal = stokBarang1.getStok();

            if (stokAwal > pengambilanBahanBaku) {
                stokBarang1.setStok(stokAwal - pengambilanBahanBaku);
                stokBarangRepository.save(stokBarang1);

                pengambilanBahanBaku = stokBarang2.getStok() - pengambilanBahanBaku;

                stokBarang2.setStok(pengambilanBahanBaku);
                stokBarangRepository.save(stokBarang2);
            } else {
                pengambilanBahanBaku = pengambilanBahanBaku - stokBarang1.getStok();
                stokBarang1.setStok(0);

                stokBarang2.setStok(stokBarang2.getStok() - pengambilanBahanBaku - stokAwal);
            }
        }
        else if (stokBarangExisting.size() == 3) {
            StokBarang stokBarang1 = stokBarangExisting.get(0);
            StokBarang stokBarang2 = stokBarangExisting.get(1);
            StokBarang stokBarang3 = stokBarangExisting.get(2);
            int stokAwal1 = stokBarang1.getStok();
            int stokAwal2 = stokBarang2.getStok();

            if (stokAwal1 > pengambilanBahanBaku) {
                stokBarang1.setStok(stokAwal1 - pengambilanBahanBaku);
                stokBarangRepository.save(stokBarang1);

                pengambilanBahanBaku = stokBarang2.getStok() - pengambilanBahanBaku;
                stokBarang2.setStok(pengambilanBahanBaku);
                stokBarangRepository.save(stokBarang2);

                pengambilanBahanBaku = stokBarang3.getStok() + pengambilanBahanBaku - stokAwal2;
                stokBarang3.setStok(pengambilanBahanBaku);
                stokBarangRepository.save(stokBarang3);
            } else {
                pengambilanBahanBaku = pengambilanBahanBaku - stokBarang1.getStok();
                stokBarang1.setStok(0);

                int stokBaru2 = stokBarang2.getStok() - pengambilanBahanBaku - stokAwal1;
                int getStokBaru2 = stokBaru2;
                if (stokBaru2 <= 0) {
                    stokBaru2 = 0;
                }
                stokBarang2.setStok(stokBaru2);
                stokBarangRepository.save(stokBarang2);

                stokBarang3.setStok(stokBarang3.getStok() - stokAwal2 + getStokBaru2);
            }
        }

        return save;
    }
}
