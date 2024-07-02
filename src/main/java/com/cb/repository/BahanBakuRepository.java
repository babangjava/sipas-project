package com.cb.repository;

import com.cb.model.BahanBaku;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BahanBakuRepository extends CrudRepository<BahanBaku, Long>, PagingAndSortingRepository<BahanBaku, Long> {
    Page<BahanBaku> findByNamaBahanContainingIgnoreCase(String name,Pageable pageable);
}
