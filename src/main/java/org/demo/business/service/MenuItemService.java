package org.demo.business.service;

import org.demo.bean.jpa.Menuitem;

import java.util.List;

public interface MenuItemService {

	Menuitem findById(Integer id) ;

	List<Menuitem> findAll();

	List<Menuitem> findByHeader(String header);

	Menuitem save(Menuitem entity);

	void delete(Integer id);


}
