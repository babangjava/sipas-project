package com.cb.repository;

import com.cb.model.OmzetCabang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaksiOmzetRepository extends CrudRepository<OmzetCabang, Long>, PagingAndSortingRepository<OmzetCabang, Long> {
    Page<OmzetCabang> findByNamaCabangContainingIgnoreCaseOrderByTglTransaksiDesc(String name, Pageable pageable);
}
