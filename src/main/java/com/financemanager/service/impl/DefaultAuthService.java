package com.financemanager.service.impl;

import com.financemanager.config.jwt.JwtProvider;
import com.financemanager.dto.UserDTO;
import com.financemanager.entity.Role;
import com.financemanager.entity.User;
import com.financemanager.entity.payload.AuthRequest;
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
    private static final Role DEFAULT_ROLE = Role.USER;
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
        newUser.setRole(DEFAULT_ROLE);
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
