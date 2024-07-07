package com.cb.repository;

import com.cb.model.Gudang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GudangRepository extends CrudRepository<Gudang, Long>, PagingAndSortingRepository<Gudang, Long> {
    Page<Gudang> findByNamaGudangContainingIgnoreCase(String namaGudang, Pageable pageable);

}
