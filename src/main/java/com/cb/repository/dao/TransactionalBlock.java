package com.cb.repository.dao;

import com.cb.dto.LaporanCabang;
import com.cb.model.Gudang;
import com.cb.model.TransaksiBahanBakuCabang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionalBlock {
    TransaksiBahanBakuCabang saveTransactionBahanBaku(TransaksiBahanBakuCabang obj);
    Gudang saveTransactionBahanBaku(Gudang obj);
    Page<LaporanCabang> laporanKeuntunganHarian(Pageable pageable);
}
