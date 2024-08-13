package com.cb.repository;

import com.cb.model.BahanBakuTerpakai;
import com.cb.model.TransaksiBahanBakuCabang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;

public interface BahanTerpakaiRepository extends CrudRepository<BahanBakuTerpakai, Long>, PagingAndSortingRepository<BahanBakuTerpakai, Long> {
    Page<BahanBakuTerpakai> findByNamaCabangContainingIgnoreCaseAndTglTransaksiBetweenOrderByTglTransaksiDesc(String name, LocalDate to, LocalDate from, Pageable pageable);
}
