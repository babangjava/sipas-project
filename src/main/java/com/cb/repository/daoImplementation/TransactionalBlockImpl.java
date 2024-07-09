package com.cb.repository.daoImplementation;

import com.cb.model.StokBarang;
import com.cb.model.TransaksiBahanBakuCabang;
import com.cb.repository.*;
import com.cb.repository.dao.TransactionalBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
        obj.setTotal(obj.getHarga()*obj.getQty());
        TransaksiBahanBakuCabang save = transaksiCabangRepository.save(obj);
        List<StokBarang> stokBarangExisting = stokBarangRepository.findByNamaGudangAndNamaBahanContainingIgnoreCaseOrderByTglTransaksiAscIdDescIdAsc(obj.getNamaGudang(), obj.getNamaBahan());

        Integer pengambilanBahanBaku = obj.getQty();
        for (int i = stokBarangExisting.size() - 1; i >= 0; i--) {
            if(stokBarangExisting.get(i).getStok()>=0) {
                if (pengambilanBahanBaku > 0) {
                    if (stokBarangExisting.get(i).getStok() >= pengambilanBahanBaku) {
                        stokBarangExisting.get(i).setStok(stokBarangExisting.get(i).getStok() - pengambilanBahanBaku);
                        pengambilanBahanBaku = 0;
                    } else {
                        pengambilanBahanBaku -= stokBarangExisting.get(i).getStok();
                        stokBarangExisting.get(i).setStok(0);
                    }
                }
            }
        }

        for (StokBarang stokBarang : stokBarangExisting) {
            stokBarangRepository.save(stokBarang);
        }

        return save;
    }
}
