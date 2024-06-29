package org.demo.data.repository.jpa;

import org.demo.bean.jpa.Uploaddocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UploadDocumentJpaRepository extends PagingAndSortingRepository<Uploaddocument, Integer> {
    @Query(value = "select new org.demo.bean.jpa.Uploaddocument(t.id, t.unitKerja, t.kategori, t.namaDocument, t.deskripsi, t.odner) from Uploaddocument t where LOWER(t.unitKerja) like LOWER(CONCAT('%',:param,'%')) OR LOWER(t.kategori) like LOWER(CONCAT('%',:param,'%')) OR LOWER(t.namaDocument) like LOWER(CONCAT('%',:param,'%')) OR LOWER(t.deskripsi) like LOWER(CONCAT('%',:param,'%')) OR LOWER(t.odner) like LOWER(CONCAT('%',:param,'%'))")
    Page<Uploaddocument> getPagging(@Param("param")String param, Pageable pageable);
}
