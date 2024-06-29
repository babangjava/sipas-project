package org.demo.data.repository.jpa;

import org.demo.bean.jpa.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserJpaRepository extends PagingAndSortingRepository<User, String> {
    User findByUser(String username);
}
