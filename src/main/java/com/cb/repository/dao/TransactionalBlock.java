package com.cb.repository.dao;

import com.cb.model.Gudang;
import com.cb.model.TransaksiBahanBakuCabang;

public interface TransactionalBlock {
    TransaksiBahanBakuCabang saveTransactionBahanBaku(TransaksiBahanBakuCabang obj);
    Gudang saveTransactionBahanBaku(Gudang obj);
}
