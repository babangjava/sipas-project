package com.cb.repository;

import com.cb.model.Cabang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CabangRepository extends CrudRepository<Cabang, Long>, PagingAndSortingRepository<Cabang, Long> {
    Page<Cabang> findByNamaCabangContainingIgnoreCase(String name, Pageable pageable);
    Cabang findByEmail(String email);
}
