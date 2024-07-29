package com.cb.repository;

import com.cb.model.TransaksiBahanBakuCabang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TransaksiCabangRepository extends CrudRepository<TransaksiBahanBakuCabang, Long>, PagingAndSortingRepository<TransaksiBahanBakuCabang, Long> {
    Page<TransaksiBahanBakuCabang> findByNamaCabangContainingIgnoreCaseAndTglTransaksiBetweenOrderByTglTransaksiDesc(String name, LocalDate to, LocalDate from, Pageable pageable);
}
