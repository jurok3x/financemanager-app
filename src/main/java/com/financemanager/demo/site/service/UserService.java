package com.financemanager.demo.site.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.financemanager.demo.site.entity.User;

@Component
public interface UserService {
	User saveUser(User user);

    void deleteUser(Integer userId);

    Optional<User> findByLogin(String login);
    
    Optional<User> findById(Integer id);
    
    Optional<User> findByLoginAndPassword(String login, String password);

    List<User> findAll();
    
    List<User> findByRoleId(Integer id);
    
    User getContextUser();
}
