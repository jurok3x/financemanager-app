package com.financemanager.demo.site.service.impl;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.financemanager.demo.site.entity.CustomUserDetails;
import com.financemanager.demo.site.entity.User;
import com.financemanager.demo.site.exception.ResourceNotFoundException;
import com.financemanager.demo.site.repository.UserRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final String USER_EMAIL_NOT_FOUND_ERROR = "User with email %s not found!";
    private final UserRepository userRepository;
	
	@Override
	public CustomUserDetails loadUserByUsername(String email) throws ResourceNotFoundException{
		User user = userRepository.findByEmail(email).orElseThrow(
				()->new ResourceNotFoundException(String.format(USER_EMAIL_NOT_FOUND_ERROR, email)));
		return CustomUserDetails.fromUserToCustomUserDetails(user);	
	}

}
