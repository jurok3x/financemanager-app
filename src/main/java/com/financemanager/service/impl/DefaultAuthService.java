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

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@PropertySource(value = { "classpath:/messages/authentification/info.properties", "classpath:/messages/user/info.properties" })
public class DefaultAuthService implements AuthService {
    
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final Role DEFAULT_ROLE = new Role(2, "ROLE_USER");
    
    @Value("${user_already_exists.error}")
    private String userAlreadyExistsError;
    @Value("${wrong_password.error}")
    private String wrongPasswordError;
    @Value("${user_email_not_found.error}")
    private String userEmailNotFoundError;
    @Value("${user_id_not_found.error}")
    private String userIdNotFoundError;

    @Override
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new BadCredentialsException(
                String.format(userEmailNotFoundError, request.getEmail())));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(
                    String.format(wrongPasswordError, request.getEmail()));
        }
        return new AuthResponse(jwtProvider.generateToken(user.getEmail(), user.getId()), "Bearer");
    }

    @Override
    public UserDTO registration(SaveUserRequest request) throws UserAlreadyExistsException {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(
                    String.format(userAlreadyExistsError, request.getEmail()));
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
                String.format(userIdNotFoundError, id)));
        user.setName(name);
        return userMapper.toUserDTO(userRepository.save(user));
    }

}
