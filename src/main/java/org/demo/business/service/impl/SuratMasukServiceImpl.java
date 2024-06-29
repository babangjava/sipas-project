package org.demo.business.service.impl;

import org.demo.bean.jpa.DocumentEntity;
import org.demo.bean.jpa.Suratmasuk;
import org.demo.business.service.SuratMasukService;
import org.demo.data.repository.jpa.DocumentJpaRepository;
import org.demo.data.repository.jpa.SuratMasukJpaRepository;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class SuratMasukServiceImpl implements SuratMasukService {

	@Resource
	private SuratMasukJpaRepository repository;

	@Resource
	private DocumentJpaRepository documentRepository;

	@Override
	public Long count() {
		return repository.count();
	}

	@Override
	public Suratmasuk findById(Integer id) {
		return repository.findOne(id);
	}

	@Override
	public List<Suratmasuk> findAll() {
		Iterable<Suratmasuk> entities = repository.findAll();
		return (List<Suratmasuk>) entities;
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
		Page<Suratmasuk> all = repository.getPagging(params.getSearch(),sortedByPriceDesc);

		Map<String,Object> map =new HashMap();
		map.put("total",all.getTotalElements());
		map.put("rows", all.getContent());

		return map;
	}

	@Override
	public List<Suratmasuk> findAllByDate(Date tanggalAwal, Date tanggalAkhir) {
		Iterable<Suratmasuk> entities = repository.getAllBetweenDates(tanggalAwal, tanggalAkhir);
		return (List<Suratmasuk>) entities;
	}

	@Override
	public Suratmasuk save(Suratmasuk entity) {
		return repository.save(entity) ;
	}

	@Override
	public Suratmasuk saveWithDocument(Suratmasuk entity) throws IOException {
		DocumentEntity document =new DocumentEntity();
		MultipartFile file = entity.getFileUpload();
		document.setName(StringUtils.cleanPath(file.getOriginalFilename()));
		document.setType(file.getContentType());
		document.setData(file.getBytes());
		DocumentEntity saveDocument = documentRepository.save(document);

		entity.setDocumentId(saveDocument.getId());
		Suratmasuk save = repository.save(entity);
		return save;
	}

	@Override
	public void delete(Integer id) {
		repository.delete(id);
	}

	public SuratMasukJpaRepository getRepository() {
		return repository;
	}

	public void setRepository(SuratMasukJpaRepository repository) {
		this.repository = repository;
	}
}
