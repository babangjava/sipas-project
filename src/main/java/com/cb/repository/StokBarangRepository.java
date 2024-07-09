package com.cb.repository;

import com.cb.model.StokBarang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StokBarangRepository extends CrudRepository<StokBarang, Long>, PagingAndSortingRepository<StokBarang, Long> {
    Page<StokBarang> findByNamaGudangContainingIgnoreCaseOrderByTglTransaksiDescIdDescIdDesc(String name, Pageable pageable);
    List<StokBarang> findByNamaGudangAndNamaBahanContainingIgnoreCaseOrderByTglTransaksiDescIdDescIdDesc(String namAgudang,String namaBahan);
    List<StokBarang> findByNamaGudangAndNamaBahanContainingIgnoreCaseOrderByTglTransaksiAscIdDescIdAsc(String namAgudang,String namaBahan);

}
