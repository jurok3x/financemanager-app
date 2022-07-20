package com.financemanager.service.impl;

import com.financemanager.config.jwt.JwtProvider;
import com.financemanager.dto.UserDTO;
import com.financemanager.entity.Role;
import com.financemanager.entity.User;
import com.financemanager.entity.payload.AuthRequest;
import com.financemanager.entity.payload.AuthResponse;
import com.financemanager.entity.payload.SaveUserRequest;
import com.financemanager.exception.UserAlreadyExistsException;
import com.financemanager.mapper.UserMapper;
import com.financemanager.repository.UserRepository;
import com.financemanager.service.AuthService;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DefaultAuthService implements AuthService {
    
    private static final String USER_ALREADY_EXISTS_ERROR = "User with email %s already exists";
    private static final String WRONG_PASSWORD_ERROR = "Wrong password for user with email %s";
    private static final String USER_EMAIL_NOT_FOUND_ERROR = "User with email - %s not found";
    private static final String USER_ID_NOT_FOUND_ERROR = "User with id %d not found";
    private static final Role DEFAULT_ROLE = Role.USER;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new BadCredentialsException(
                String.format(USER_EMAIL_NOT_FOUND_ERROR, request.getEmail())));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(
                    String.format(WRONG_PASSWORD_ERROR, request.getEmail()));
        }
        return new AuthResponse(jwtProvider.generateToken(user.getEmail()), "Bearer");
    }

    @Override
    public UserDTO registration(SaveUserRequest request) throws UserAlreadyExistsException {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(
                    String.format(USER_ALREADY_EXISTS_ERROR, request.getEmail()));
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(DEFAULT_ROLE);
        return userMapper.toUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO updateName(String name, Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new BadCredentialsException(
                String.format(USER_ID_NOT_FOUND_ERROR, id)));
        user.setName(name);
        return userMapper.toUserDTO(userRepository.save(user));
    }

}
