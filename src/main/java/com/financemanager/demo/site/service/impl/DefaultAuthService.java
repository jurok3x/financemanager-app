package com.financemanager.demo.site.service.impl;

import com.financemanager.demo.site.config.jwt.JwtProvider;
import com.financemanager.demo.site.dto.UserDTO;
import com.financemanager.demo.site.entity.Role;
import com.financemanager.demo.site.entity.User;
import com.financemanager.demo.site.entity.payload.AuthRequest;
import com.financemanager.demo.site.entity.payload.SaveUserRequest;
import com.financemanager.demo.site.exception.UserAlreadyExistsException;
import com.financemanager.demo.site.mapper.UserMapper;
import com.financemanager.demo.site.repository.UserRepository;
import com.financemanager.demo.site.service.AuthService;

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
    private static final Integer DEFAULT_ROLE_ID = 1;
    private static final String DEFAULT_ROLE_NAME = "ROLE_USER";
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new BadCredentialsException(
                String.format(USER_EMAIL_NOT_FOUND_ERROR, request.getEmail())));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(
                    String.format(WRONG_PASSWORD_ERROR, request.getEmail()));
        }
        return jwtProvider.generateToken(user.getEmail());
    }

    @Override
    public UserDTO registration(SaveUserRequest request) throws UserAlreadyExistsException {
        if(userRepository.findByEmail(request.getLogin()).isPresent()) {
            throw new UserAlreadyExistsException(
                    String.format(USER_ALREADY_EXISTS_ERROR, request.getLogin()));
        }
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setName(request.getName());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(new Role(DEFAULT_ROLE_ID, DEFAULT_ROLE_NAME));
        return userMapper.toUserDTO(userRepository.save(newUser));
    }

    @Override
    public UserDTO updateUser(SaveUserRequest request, Integer id) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new BadCredentialsException(
                String.format(USER_EMAIL_NOT_FOUND_ERROR, request.getLogin())));
        user.setName(request.getName());
        return userMapper.toUserDTO(userRepository.save(user));
    }

}
