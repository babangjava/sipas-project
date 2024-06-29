package org.demo.business.service;

import org.demo.bean.jpa.User;

import java.util.List;

public interface UserService {

	Long count() ;

	User findById(String id) ;

	User findByUsername(String username) ;

	List<User> findAll();

	User save(User entity);

	void delete(String id);


}
