package org.demo.business.service;


import org.demo.bean.jpa.Suratkeluar;
import org.demo.web.common.AdvanceSearch;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SuratKeluarService {

	Long count() ;

	Suratkeluar findById(Integer id) ;

	List<Suratkeluar> findAll();

	Map<String,Object> findAll(AdvanceSearch params);

	List<Suratkeluar> findAllByDate(Date tanggalAwal,Date tanggalAkhir);

	Suratkeluar save(Suratkeluar entity);

	Suratkeluar saveWithDocument(Suratkeluar entity) throws IOException;

	void delete(Integer id);


}
