package org.demo.business.service;


import org.demo.bean.jpa.Suratmasuk;
import org.demo.web.common.AdvanceSearch;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SuratMasukService {

	Long count() ;

	Suratmasuk findById(Integer id) ;

	List<Suratmasuk> findAll();

	Map<String,Object> findAll(AdvanceSearch params);

	List<Suratmasuk> findAllByDate(Date tanggalAwal,Date tanggalAkhir);

	Suratmasuk save(Suratmasuk entity);

	Suratmasuk saveWithDocument(Suratmasuk entity) throws IOException;

	void delete(Integer id);


}
