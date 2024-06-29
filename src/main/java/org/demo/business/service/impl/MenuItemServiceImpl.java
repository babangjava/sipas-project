package org.demo.business.service.impl;

import org.demo.bean.jpa.Menuitem;
import org.demo.business.service.MenuItemService;
import org.demo.data.repository.jpa.MenuItemJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

	@Resource
	private MenuItemJpaRepository repository;

	@Override
	public Menuitem findById(Integer id) {
		return repository.findOne(id);
	}

	@Override
	public List<Menuitem> findAll() {
		Iterable<Menuitem> entities = repository.findAll(new Sort(Sort.Direction.DESC, "idMenu"));
		return (List<Menuitem>) entities;
	}

	@Override
	public List<Menuitem> findByHeader(String header) {
		return repository.findByHeader(header);
	}

	@Override
	public Menuitem save(Menuitem entity) {
		return repository.save(entity) ;
	}

	@Override
	public void delete(Integer id) {
		repository.delete(id);
	}

	public MenuItemJpaRepository getRepository() {
		return repository;
	}

	public void setRepository(MenuItemJpaRepository repository) {
		this.repository = repository;
	}
}
