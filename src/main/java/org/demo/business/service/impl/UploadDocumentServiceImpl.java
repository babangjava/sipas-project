package org.demo.business.service.impl;

import org.demo.bean.jpa.Uploaddocument;
import org.demo.business.service.UploadDocumentService;
import org.demo.data.repository.jpa.UploadDocumentJpaRepository;
import org.demo.web.common.AdvanceSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class UploadDocumentServiceImpl implements UploadDocumentService {

	@Resource
	private UploadDocumentJpaRepository repository;

	@Override
	public Long count() {
		return repository.count();
	}

	@Override
	public Uploaddocument findById(Integer id) {
		return repository.findOne(id);
	}

	@Override
	public List<Uploaddocument> findAll() {
		Iterable<Uploaddocument> entities = repository.findAll();
		return (List<Uploaddocument>) entities;
	}

	@Override
	public Map<String, Object> findAll(AdvanceSearch params) {
		if(params.getSort()==null){
			params.setOrder("DESC");
			params.setSort("id");
		}

		if(params.getSearch()==null){
			params.setSearch("");
		}

		int page = params.getOffset() / params.getLimit();

		PageRequest sortedByPriceDesc = new PageRequest(page,params.getLimit(), Sort.Direction.fromString(params.getOrder()), params.getSort());
		Page<Uploaddocument> all = repository.getPagging(params.getSearch(),sortedByPriceDesc);

		Map<String,Object> map =new HashMap();
		map.put("total",all.getTotalElements());
		map.put("rows", all.getContent());

		return map;
	}

	@Override
	public Uploaddocument save(Uploaddocument entity) throws IOException {
		if(!entity.getFileUpload().isEmpty()){
			MultipartFile file = entity.getFileUpload();
			entity.setName(StringUtils.cleanPath(file.getOriginalFilename()));
			entity.setType(file.getContentType());
			entity.setData(file.getBytes());
		}
		return repository.save(entity) ;
	}

	@Override
	public void delete(Integer id) {
		repository.delete(id);
	}

	public UploadDocumentJpaRepository getRepository() {
		return repository;
	}

	public void setRepository(UploadDocumentJpaRepository repository) {
		this.repository = repository;
	}
}
