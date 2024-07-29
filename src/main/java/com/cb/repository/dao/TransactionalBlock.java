package com.cb.repository.dao;

import com.cb.dto.LaporanCabang;
import com.cb.model.Gudang;
import com.cb.model.TransaksiBahanBakuCabang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TransactionalBlock {
    TransaksiBahanBakuCabang saveTransactionBahanBaku(TransaksiBahanBakuCabang obj);
    Gudang saveTransactionBahanBaku(Gudang obj);
    Page<LaporanCabang> laporanKeuntunganHarian(Pageable pageable);
    Page<LaporanCabang> laporanKeuntunganBulanan(int bulan, Pageable pageable);
    Page<LaporanCabang> laporanKeuntunganBulananShort(Pageable pageable);

}
