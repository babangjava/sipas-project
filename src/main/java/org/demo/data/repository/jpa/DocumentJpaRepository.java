package org.demo.data.repository.jpa;

import org.demo.bean.jpa.DocumentEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DocumentJpaRepository extends PagingAndSortingRepository<DocumentEntity, Integer> {

}
