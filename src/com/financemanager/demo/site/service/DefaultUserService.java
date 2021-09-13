package com.financemanager.demo.site.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.financemanager.demo.site.entity.Role;
import com.financemanager.demo.site.entity.User;
import com.financemanager.demo.site.exception.ResourceNotFoundException;
import com.financemanager.demo.site.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DefaultUserService implements UserService{

	private final UserRepository userRepository;
	private final RoleService roleService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public User saveUser(User user) {
		Role role = roleService.findByName("ROLE_USER").orElseThrow(
				()->new ResourceNotFoundException("Role with Name ROLE_USER Not Found!"));
		user.setRole(role);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	@Override
	public Optional<User> findById(Integer id) {
		return userRepository.findById(id);
	}

	@Override
	public void deleteUser(Integer userId) {
		userRepository.deleteById(userId);		
	}

	@Override
	public Optional<User> findByLogin(String login){
		return userRepository.findByLogin(login);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public List<User> findByRoleId(Integer id) {
		return  userRepository.findByRoleId(id);
	}

	@Override
	public Optional<User> findByLoginAndPassword(String login, String password) {
		Optional<User> user = userRepository.findByLogin(login);
		if (user.isPresent()) {
			if(passwordEncoder.matches(password, user.get().getPassword()))
				return user;
        }
		return Optional.empty();
	}
	
	@Override
	public User getContextUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		return userRepository.findByLogin(username).orElseThrow(
				()->new ResourceNotFoundException("User with Name :" + username +" Not Found!"));
	}

}
