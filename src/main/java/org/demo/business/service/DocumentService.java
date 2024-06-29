package org.demo.business.service;


import org.demo.bean.jpa.DocumentEntity;

import java.util.List;

public interface DocumentService {

	DocumentEntity findById(Integer id) ;

	List<DocumentEntity> findAll();

	DocumentEntity save(DocumentEntity entity);

	void delete(Integer id);


}
