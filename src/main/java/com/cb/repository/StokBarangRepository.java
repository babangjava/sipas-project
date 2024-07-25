package com.cb.repository;

import com.cb.model.StokBarang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StokBarangRepository extends CrudRepository<StokBarang, Long>, PagingAndSortingRepository<StokBarang, Long> {
    @Query(value = "SELECT stok FROM stok_barang where nama_gudang =:namaGudang AND nama_bahan=:namaBahan", nativeQuery = true)
    Integer getStokBarangTerakhir(@Param("namaGudang") String namAgudang, @Param("namaBahan") String namaBahan);
    List<StokBarang> findByNamaGudangAndNamaBahanContainingIgnoreCaseOrderByTglTransaksiAscIdDescIdAsc(String namAgudang,String namaBahan);

}
