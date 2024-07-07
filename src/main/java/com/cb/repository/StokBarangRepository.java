package com.cb.repository;

import com.cb.model.Gudang;
import com.cb.model.StokBarang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StokBarangRepository extends CrudRepository<StokBarang, Long>, PagingAndSortingRepository<StokBarang, Long> {
    Page<StokBarang> findByNamaGudangContainingIgnoreCase(String name, Pageable pageable);

}
