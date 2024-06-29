package org.demo.business.service;


import org.demo.bean.jpa.Uploaddocument;
import org.demo.web.common.AdvanceSearch;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UploadDocumentService {

	Long count() ;

	Uploaddocument findById(Integer id) ;

	List<Uploaddocument> findAll();

	Map<String,Object> findAll(AdvanceSearch params);

	Uploaddocument save(Uploaddocument entity) throws IOException;

	void delete(Integer id);


}
