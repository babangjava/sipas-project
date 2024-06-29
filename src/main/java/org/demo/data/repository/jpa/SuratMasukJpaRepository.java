package org.demo.data.repository.jpa;

import org.demo.bean.jpa.Suratmasuk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface SuratMasukJpaRepository extends PagingAndSortingRepository<Suratmasuk, Integer> {
    @Query(value = "select t from Suratmasuk t where t.tanggal BETWEEN :startDate AND :endDate")
    List<Suratmasuk> getAllBetweenDates(@Param("startDate")Date startDate,@Param("endDate")Date endDate);

    @Query(value = "select t from Suratmasuk t where LOWER(t.prihal) like LOWER(CONCAT('%',:noSurat,'%')) OR LOWER(t.noSurat) like LOWER(CONCAT('%',:noSurat,'%')) OR LOWER(t.tujuan) like LOWER(CONCAT('%',:noSurat,'%')) OR LOWER(t.odner) like LOWER(CONCAT('%',:noSurat,'%'))")
    Page<Suratmasuk> getPagging(@Param("noSurat")String noSurat, Pageable pageable);
}
