package com.financemanager.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.financemanager.config.jwt.JwtProvider;
import com.financemanager.entity.Role;
import com.financemanager.entity.User;
import com.financemanager.entity.payload.AuthRequest;
import com.financemanager.entity.payload.SaveUserRequest;
import com.financemanager.exception.UserAlreadyExistsException;
import com.financemanager.mapper.RoleMapper;
import com.financemanager.mapper.UserMapper;
import com.financemanager.repository.UserRepository;
import com.financemanager.service.AuthService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

@ExtendWith(value = { MockitoExtension.class })
class DefaultAuthServiceTest {
    
    private static final String USER_PASSWORD = "metro2033";
    
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtProvider jwtProvider;
    private AuthService authService;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    
    @BeforeEach
    void setUp() {
        userMapper = new UserMapper(new RoleMapper());
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new DefaultAuthService(userMapper, userRepository, jwtProvider, passwordEncoder);
        ReflectionTestUtils.setField(authService, "badPasswordError", "Wrong password for user with email {}");
        ReflectionTestUtils.setField(authService, "userAlreadyExistsError", "User with email {} already exists");
        ReflectionTestUtils.setField(authService, "userEmailNotFoundError", "User with email {} not found");
        ReflectionTestUtils.setField(authService, "userIdNotFoundError", "User with id {} not found");
    }
    
    @Test
    void whenLogin_thenReturnToken() {
        User user = prepareUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        given(userRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(user));
        given(jwtProvider.generateToken(Mockito.anyString())).willReturn("token");
        AuthRequest request = new AuthRequest(user.getEmail(), USER_PASSWORD);
        assertEquals("token", authService.login(request).getToken());
        verify(userRepository).findByEmail(request.getEmail());
        verify(jwtProvider).generateToken(request.getEmail());
    }
    
    @Test
    void whenEmailNotFound_thenThrowException() {
        given(userRepository.findByEmail(Mockito.anyString())).willReturn(Optional.empty());
        AuthRequest request = new AuthRequest("emptyemail", USER_PASSWORD);
        assertThrows(BadCredentialsException.class, () -> authService.login(request));
        verify(userRepository).findByEmail(request.getEmail());
        verifyNoInteractions(jwtProvider);
    }
    
    @Test
    void whenPasswordWrong_thenThrowException() {
        User user = prepareUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        given(userRepository.findByEmail(Mockito.anyString())).willReturn(Optional.empty());
        
        AuthRequest request = new AuthRequest(user.getEmail(), "wrong_password");
        assertThrows(BadCredentialsException.class, () -> authService.login(request));
        verify(userRepository).findByEmail(request.getEmail());
        verifyNoInteractions(jwtProvider);
    }
    
    @Test
    void whenSaveUser_thenReturnCorrectResult() throws UserAlreadyExistsException {
        User user = prepareUser();
        given(userRepository.findByEmail(Mockito.anyString())).willReturn(Optional.empty());
        given(userRepository.save(Mockito.any(User.class))).willReturn(user);
        SaveUserRequest request = new SaveUserRequest(user.getName(), user.getPassword(), user.getEmail());
        assertEquals(userMapper.toUserDTO(user), authService.registration(request));
        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository).save(Mockito.any(User.class));
    }
    
    @Test
    void whenUserExist_thenThrowException() throws UserAlreadyExistsException {
        User user = prepareUser();
        given(userRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(user));
        SaveUserRequest request = new SaveUserRequest(user.getName(), user.getPassword(), user.getEmail());
        assertThrows(UserAlreadyExistsException.class, () -> authService.registration(request));
        verify(userRepository).findByEmail(request.getEmail());
    }
    
    @Test
    void whenUpdateUser_thenReturnCorrectResult() {
        User user = prepareUser();
        given(userRepository.findById(Mockito.anyInt())).willReturn(Optional.of(user));
        given(userRepository.save(Mockito.any(User.class))).willReturn(user);
        assertEquals(userMapper.toUserDTO(user), authService.updateName(user.getName(), user.getId()));
        verify(userRepository).findById(user.getId());
        verify(userRepository).save(Mockito.any(User.class));
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(userRepository);
    }
    
    private User prepareUser() {
        User user = new User();
        user.setId(1);
        user.setEmail("jurok3x@gmail.com");
        user.setName("Yurii");
        user.setPassword(USER_PASSWORD);
        user.setRole(new Role(2, "ROLE_USER"));
        return user;
    }

}
