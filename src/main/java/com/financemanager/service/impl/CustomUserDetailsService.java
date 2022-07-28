package com.financemanager.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.financemanager.entity.CustomUserDetails;
import com.financemanager.entity.User;
import com.financemanager.exception.ResourceNotFoundException;
import com.financemanager.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@PropertySource(value = { "classpath:/messages/authentification/info.properties" })
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
	
    @Value("${user_email_not_found.error}")
    private String userEmailNotFoundError;
    
	@Override
	public CustomUserDetails loadUserByUsername(String email) throws ResourceNotFoundException{
		User user = userRepository.findByEmail(email).orElseThrow(
				()->new ResourceNotFoundException(String.format(userEmailNotFoundError, email)));
		return CustomUserDetails.fromUserToCustomUserDetails(user);	
	}

}
