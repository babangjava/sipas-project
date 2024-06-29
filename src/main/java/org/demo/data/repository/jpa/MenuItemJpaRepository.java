package org.demo.data.repository.jpa;

import org.demo.bean.jpa.Menuitem;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MenuItemJpaRepository extends PagingAndSortingRepository<Menuitem, Integer> {
    List<Menuitem> findByHeader(String header);
}
