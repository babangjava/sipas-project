package org.demo.business.service.impl;

import org.demo.bean.jpa.DocumentEntity;
import org.demo.business.service.DocumentService;
import org.demo.data.repository.jpa.DocumentJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component
@Transactional
public class DocumentServiceImpl implements DocumentService {

	@Resource
	private DocumentJpaRepository repository;

	@Override
	public DocumentEntity findById(Integer id) {
		return repository.findOne(id);
	}

	@Override
	public List<DocumentEntity> findAll() {
		Iterable<DocumentEntity> entities = repository.findAll();
		return (List<DocumentEntity>) entities;
	}

	@Override
	public DocumentEntity save(DocumentEntity entity) {
		return repository.save(entity) ;
	}

	@Override
	public void delete(Integer id) {
		repository.delete(id);
	}

	public DocumentJpaRepository getRepository() {
		return repository;
	}

	public void setRepository(DocumentJpaRepository repository) {
		this.repository = repository;
	}
}
