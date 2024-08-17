package com.cb.repository.dao;

import com.cb.dto.LaporanCabang;
import com.cb.model.BahanBakuTerpakai;
import com.cb.model.Gudang;
import com.cb.model.TransaksiBahanBakuCabang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TransactionalBlock {
    TransaksiBahanBakuCabang saveTransactionBahanBaku(TransaksiBahanBakuCabang obj);
    Gudang saveTransactionBahanBaku(Gudang obj);
    Page<LaporanCabang> laporanKeuntunganHarian(Pageable pageable);
    Page<BahanBakuTerpakai> laporanStokTerpakai(String namaCabang,Pageable pageable);
    Page<TransaksiBahanBakuCabang> laporanStokSisa(String namaCabang,Pageable pageable);
    Page<LaporanCabang> laporanKeuntunganBulanan(String bulan, Pageable pageable);
    Page<String> laporanKeuntunganBulananShort(Pageable pageable);
    BahanBakuTerpakai saveTransactionBahanTerpakai(BahanBakuTerpakai obj);

}
