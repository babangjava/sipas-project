package com.cb.repository;

import com.cb.model.PengeluaranCabang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PengeluaranRepository extends CrudRepository<PengeluaranCabang, Long>, PagingAndSortingRepository<PengeluaranCabang, Long> {
    Page<PengeluaranCabang> findByNamaCabangContainingIgnoreCaseOrderByTglTransaksiDesc(String name, Pageable pageable);
    Page<PengeluaranCabang> findAllByOrderByTglTransaksiDesc(Pageable pageable);
}
