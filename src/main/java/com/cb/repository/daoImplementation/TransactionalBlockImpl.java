package com.cb.repository.daoImplementation;

import com.cb.model.BahanBaku;
import com.cb.model.Gudang;
import com.cb.model.StokBarang;
import com.cb.model.TransaksiBahanBakuCabang;
import com.cb.repository.StokBarangRepository;
import com.cb.repository.TransaksiCabangRepository;
import com.cb.repository.dao.TransactionalBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Override
    public Gudang saveTransactionBahanBaku(Gudang obj) {
        List<StokBarang> stokBarangList =new ArrayList<StokBarang>();
        for (BahanBaku item : obj.getBahanBakuList()) {
            if(item.getQty()!=0){
                StokBarang stokBarang =new StokBarang();
                stokBarang.setId(null);
                stokBarang.setTglTransaksi(LocalDate.now());
                stokBarang.setNamaGudang(obj.getNamaGudang());
                //setup nbahan
                stokBarang.setNamaBahan(item.getNamaBahan());
                stokBarang.setType(item.getType());
                stokBarang.setHarga(item.getHarga());
                stokBarang.setQty(item.getQty());

                Integer stokBarangTerakhir = stokBarangRepository.getStokBarangTerakhir(obj.getNamaGudang(), item.getNamaBahan());
                if(stokBarangTerakhir==null){
                    stokBarangTerakhir=0;
                }
                stokBarang.setStok(item.getQty()+ stokBarangTerakhir);

                stokBarangList.add(stokBarang);
            }
        }
        stokBarangRepository.saveAll(stokBarangList);
        return obj;
    }
}
