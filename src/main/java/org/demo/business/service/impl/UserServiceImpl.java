package org.demo.business.service.impl;

import org.demo.bean.jpa.User;
import org.demo.business.service.UserService;
import org.demo.data.repository.jpa.UserJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component
@Transactional
public class UserServiceImpl implements UserService {

	@Resource
	private UserJpaRepository repository;

	@Override
	public Long count() {
		return repository.count();
	}

	@Override
	public User findById(String id) {
		return repository.findOne(id);
	}

	@Override
	public User findByUsername(String username) {
		return repository.findByUser(username);
	}

	@Override
	public List<User> findAll() {
		Iterable<User> entities = repository.findAll();
		return (List<User>) entities;
	}

	@Override
	public User save(User entity) {
		return repository.save(entity) ;
	}

	@Override
	public void delete(String id) {
		repository.delete(id);
	}

	public UserJpaRepository getRepository() {
		return repository;
	}

	public void setRepository(UserJpaRepository repository) {
		this.repository = repository;
	}
}
