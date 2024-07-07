package com.cb.repository;

import com.cb.model.TransaksiBahanBakuCabang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaksiCabangRepository extends CrudRepository<TransaksiBahanBakuCabang, Long>, PagingAndSortingRepository<TransaksiBahanBakuCabang, Long> {
    Page<TransaksiBahanBakuCabang> findByNamaCabangContainingIgnoreCaseOrderByTglTransaksiDesc(String name, Pageable pageable);
}
