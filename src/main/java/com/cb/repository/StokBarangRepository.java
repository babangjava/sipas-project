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
    Page<StokBarang> findByNamaGudangContainingIgnoreCase(String name, Pageable pageable);
    @Query(value = "SELECT * FROM stok_barang WHERE nama_gudang = :namAgudang AND nama_bahan = :namaBahan AND stok != 0 ORDER by tgl_transaksi ASC, id ASC", nativeQuery = true) // @Query(value = "UPD User u SET u.email = :email WHERE u.id = :id", nativeQuery = true)
    List<StokBarang> getStokBarangExisting(@Param("namAgudang") String namAgudang, @Param("namaBahan") String namaBahan);
    List<StokBarang> findByNamaGudangAndNamaBahanContainingIgnoreCaseOrderByTglTransaksiAscIdDescIdAsc(String namAgudang,String namaBahan);

}
