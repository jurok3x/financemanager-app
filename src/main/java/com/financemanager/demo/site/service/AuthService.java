package com.financemanager.demo.site.service;

import com.financemanager.demo.site.dto.UserDTO;
import com.financemanager.demo.site.entity.payload.AuthRequest;
import com.financemanager.demo.site.entity.payload.SaveUserRequest;
import com.financemanager.demo.site.exception.UserAlreadyExistsException;

public interface AuthService {
    
    String login(AuthRequest request);
    UserDTO registration(SaveUserRequest request) throws UserAlreadyExistsException;
    UserDTO updateUser(SaveUserRequest request, Integer id);
}
