package com.financemanager.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.financemanager.entity.CustomUserDetails;
import com.financemanager.entity.Role;
import com.financemanager.entity.User;
import com.financemanager.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(value = { MockitoExtension.class })
class CustomUserDetailsServiceTest {
    
   @Mock
   private UserRepository userRepository;
   
   @InjectMocks
   private CustomUserDetailsService userDetailsService;
   
   @Test
   void whenFindByEmail_thenReturnCorrectResult() {
       User user = prepareUser();
       given(userRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(user));
       assertEquals(CustomUserDetails.fromUserToCustomUserDetails(user).getUsername(), userDetailsService.loadUserByUsername(user.getEmail()).getUsername());
       verify(userRepository).findByEmail(user.getEmail());
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
       user.setPassword("metro2033");
       user.setRole(Role.ADMIN);
       return user;
   }
}
