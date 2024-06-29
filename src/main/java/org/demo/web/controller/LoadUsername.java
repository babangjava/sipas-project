package org.demo.web.controller;
import org.demo.bean.jpa.User;
import org.demo.data.repository.jpa.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

public class LoadUsername implements UserDetailsService {

    @Autowired
    private UserJpaRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userRepository.findByUser(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
        String role = user.getRole();
        if(role.equals("Admin")){
            role = "ROLE_ADMIN";
        }else{
            role = "ROLE_USER";
        }
        grantedAuthorities.add(new SimpleGrantedAuthority(role));

        return new org.springframework.security.core.userdetails.User(user.getUser(), user.getPassword(), grantedAuthorities);
    }
}
