package com.financemanager.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.financemanager.dto.UserDTO;
import com.financemanager.mapper.UserMapper;
import com.financemanager.repository.UserRepository;
import com.financemanager.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DefaultUserService implements UserService{

	private static final String USER_EMAIL_NOT_FOUND_ERROR = "User with email %s not found";
    private static final String USER_ID_NOT_FOUND_ERROR = "User with id %d not found";
    private final UserRepository userRepository;
	private final UserMapper userMapper;
	
	
	@Override
	public Optional<UserDTO> findById(Integer id) {
	    UserDTO userDTO = userRepository.findById(id).map(userMapper::toUserDTO).orElseThrow(() -> new BadCredentialsException(
                String.format(USER_ID_NOT_FOUND_ERROR, id)));
		return Optional.ofNullable(userDTO);
	}

	@Override
	public void delete(Integer userId) {
		userRepository.deleteById(userId);		
	}

	@Override
    public Optional<UserDTO> findByEmail(String email){
        UserDTO userDTO = userRepository.findByEmail(email).map(userMapper::toUserDTO).orElseThrow(() -> new BadCredentialsException(
                String.format(USER_EMAIL_NOT_FOUND_ERROR, email)));
        return Optional.ofNullable(userDTO);
    }

	@Override
	public List<UserDTO> findAll() {
		return userRepository.findAll().stream().map(userMapper::toUserDTO).collect(Collectors.toList());
	}

}
