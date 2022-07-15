package com.financemanager.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.financemanager.entity.Role;
import com.financemanager.entity.User;
import com.financemanager.mapper.UserMapper;
import com.financemanager.repository.UserRepository;
import com.financemanager.service.UserService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(value = { MockitoExtension.class })
class DefaultUserServiceTest {

    @Mock
    private UserRepository userRepository;
    private UserService userService;
    private UserMapper userMapper;
    
    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
        userService = new DefaultUserService(userRepository, userMapper);
    }
    
    @Test
    void whenFindById_thenReturnCorrectResult() {
       User user = prepareUser();
       given(userRepository.findById(Mockito.anyInt())).willReturn(Optional.of(user));
       assertEquals(Optional.of(userMapper.toUserDTO(user)), userService.findById(user.getId()));
       verify(userRepository).findById(user.getId());
    }
    
    @Test
    void whenFindByEmail_thenReturnCorrectResult() {
       User user = prepareUser();
       given(userRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(user));
       assertEquals(Optional.of(userMapper.toUserDTO(user)), userService.findByEmail(user.getEmail()));
       verify(userRepository).findByEmail(user.getEmail());
    }
    
    @Test
    void whenFindAll_thenReturnCorrectResult() {
       List<User> users = Arrays.asList(prepareUser());
       given(userRepository.findAll()).willReturn(users);
       assertEquals(users.stream().map(userMapper::toUserDTO).collect(Collectors.toList()), userService.findAll());
       verify(userRepository).findAll();
    }
    
    @Test
    void whenFindByCategoryId_thenReturnCorrectResult() {
       List<User> users = Arrays.asList(prepareUser());
       given(userRepository.findByCategoryId(Mockito.anyInt())).willReturn(users);
       assertEquals(users.stream().map(userMapper::toUserDTO).collect(Collectors.toList()), userService.findByCategoryId(1));
       verify(userRepository).findByCategoryId(1);
    }
    
    @Test
    void whenDelete_thenReturnCorrectResult() {
       User user = prepareUser();
       given(userRepository.findById(Mockito.anyInt())).willReturn(Optional.of(user));
       userService.delete(user.getId());
       verify(userRepository).deleteById(user.getId());
       verify(userRepository).findById(user.getId());
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
        user.setPassword("metro090");
        user.setRole(Role.ADMIN);
        return user;
    }

}
