package com.financemanager.service;

import java.util.List;

import com.financemanager.dto.UserDTO;

import org.springframework.stereotype.Component;

@Component
public interface UserService {

    void delete(Integer id);
    
    UserDTO findByEmail(String email);
    
    UserDTO findById(Integer id);

    List<UserDTO> findAll();
    
    List<UserDTO> findByCategoryId(Integer categoryId);

}