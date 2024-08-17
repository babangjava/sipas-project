package com.cb.repository;

import com.cb.model.StokBarang;
import com.cb.model.TransaksiBahanBakuCabang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransaksiCabangRepository extends CrudRepository<TransaksiBahanBakuCabang, Long>, PagingAndSortingRepository<TransaksiBahanBakuCabang, Long> {
    Page<TransaksiBahanBakuCabang> findByNamaCabangContainingIgnoreCaseAndTglTransaksiBetweenOrderByTglTransaksiDesc(String name, LocalDate to, LocalDate from, Pageable pageable);
    List<TransaksiBahanBakuCabang> findByNamaGudangContainingIgnoreCaseOrderById(@Param("namaGudang") String namaGudang);
    List<TransaksiBahanBakuCabang> findByNamaGudangAndNamaCabangAndNamaBahanAndTglTransaksi(String namaGudang, String namaCabang, String namaBahan, LocalDate to);
}
