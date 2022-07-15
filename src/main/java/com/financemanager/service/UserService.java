package com.financemanager.service;

import java.util.List;
import java.util.Optional;

import com.financemanager.dto.UserDTO;

import org.springframework.stereotype.Component;

@Component
public interface UserService {

    void delete(Integer userId);
    
    Optional<UserDTO> findByEmail(String email);
    
    Optional<UserDTO> findById(Integer id);

    List<UserDTO> findAll();

}
