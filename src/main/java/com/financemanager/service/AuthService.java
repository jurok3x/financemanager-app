package com.financemanager.service;

import com.financemanager.dto.UserDTO;
import com.financemanager.entity.payload.AuthRequest;
import com.financemanager.entity.payload.AuthResponse;
import com.financemanager.entity.payload.SaveUserRequest;
import com.financemanager.exception.UserAlreadyExistsException;

public interface AuthService {
    
    AuthResponse login(AuthRequest request);
    
    UserDTO registration(SaveUserRequest request) throws UserAlreadyExistsException;
    
    UserDTO updateName(String name, Integer id);
}
