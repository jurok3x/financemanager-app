package com.financemanager.demo.site.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.financemanager.demo.site.dto.UserDTO;

@Component
public interface UserService {

    void delete(Integer userId);
    
    Optional<UserDTO> findByEmail(String email);
    
    Optional<UserDTO> findById(Integer id);

    List<UserDTO> findAll();
    
    List<UserDTO> findByRoleId(Integer id);

}
