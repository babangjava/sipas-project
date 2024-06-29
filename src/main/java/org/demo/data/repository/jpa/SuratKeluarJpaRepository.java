package org.demo.data.repository.jpa;

import org.demo.bean.jpa.Suratkeluar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface SuratKeluarJpaRepository extends PagingAndSortingRepository<Suratkeluar, Integer> {
    @Query(value = "select t from Suratkeluar t where t.tanggal BETWEEN :startDate AND :endDate")
    List<Suratkeluar> getAllBetweenDates(@Param("startDate")Date startDate,@Param("endDate")Date endDate);

    @Query(value = "select t from Suratkeluar t where LOWER(t.prihal) like LOWER(CONCAT('%',:noSurat,'%')) OR LOWER(t.noSurat) like LOWER(CONCAT('%',:noSurat,'%')) OR LOWER(t.departement) like LOWER(CONCAT('%',:noSurat,'%')) OR LOWER(t.odner) like LOWER(CONCAT('%',:noSurat,'%'))")
    Page<Suratkeluar> getPagging(@Param("noSurat")String noSurat, Pageable pageable);
}
