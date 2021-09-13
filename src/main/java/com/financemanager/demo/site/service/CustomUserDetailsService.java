package com.financemanager.demo.site.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.financemanager.demo.site.entity.CustomUserDetails;
import com.financemanager.demo.site.entity.User;
import com.financemanager.demo.site.exception.ResourceNotFoundException;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
    private UserService userService;
	
	@Override
	public CustomUserDetails loadUserByUsername(String username) throws ResourceNotFoundException{
		User user = userService.findByLogin(username).orElseThrow(
				()->new ResourceNotFoundException("User with Name :" + username +" Not Found!"));
		System.out.println("Password is " + user.getPassword());
		return CustomUserDetails.fromUserToCustomUserDetails(user);	
	}

}
