package com.cb.repository.daoImplementation;

import com.cb.model.BahanBaku;
import com.cb.model.TransaksiBahanBakuCabang;
import com.cb.repository.StokBarangRepository;
import com.cb.repository.TransaksiCabangRepository;
import com.cb.repository.dao.TransactionalBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        List<TransaksiBahanBakuCabang> transaksiBahanBakuCabangList =new ArrayList<TransaksiBahanBakuCabang>();
        for (BahanBaku item : obj.getBahanBakuList()) {
            TransaksiBahanBakuCabang transaksiBahanBakuCabang =new TransaksiBahanBakuCabang();
            transaksiBahanBakuCabang.setId(null);
            transaksiBahanBakuCabang.setTglTransaksi(obj.getTglTransaksi());
            transaksiBahanBakuCabang.setNamaGudang(obj.getNamaGudang());
            transaksiBahanBakuCabang.setNamaCabang(obj.getNamaCabang());
            //setup nbahan
            transaksiBahanBakuCabang.setNamaBahan(item.getNamaBahan());
            transaksiBahanBakuCabang.setType(item.getType());
            transaksiBahanBakuCabang.setHarga(item.getHarga());
            transaksiBahanBakuCabang.setQty(item.getQty());
            transaksiBahanBakuCabang.setTotal(item.getQty()*item.getHarga());

            transaksiBahanBakuCabangList.add(transaksiBahanBakuCabang);
        }

        transaksiCabangRepository.saveAll(transaksiBahanBakuCabangList);
        return obj;
    }
}
