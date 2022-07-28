package com.financemanager.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.financemanager.dto.UserDTO;
import com.financemanager.mapper.UserMapper;
import com.financemanager.repository.UserRepository;
import com.financemanager.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@PropertySource(value = { "classpath:/messages/authentification/info.properties" })
public class DefaultUserService implements UserService{

    private final UserRepository userRepository;
	private final UserMapper userMapper;
	
	@Value("${user_email_not_found.error}")
	private String userEmailNotFoundError;
	@Value("${user_id_not_found.error}")
    private String userIdNotFoundError;
	
	
	@Override
	public UserDTO findById(Integer id) {
	    return userRepository.findById(id).map(userMapper::toUserDTO).orElseThrow(() -> new BadCredentialsException(
                String.format(userIdNotFoundError, id)));
	}

	@Override
	public void delete(Integer id) {
	    UserDTO userDTO = userRepository.findById(id).map(userMapper::toUserDTO).orElseThrow(() -> new BadCredentialsException(
                String.format(userIdNotFoundError, id)));
		userRepository.deleteById(userDTO.getId());		
	}

	@Override
    public UserDTO findByEmail(String email){
	    return userRepository.findByEmail(email).map(userMapper::toUserDTO).orElseThrow(() -> new BadCredentialsException(
                String.format(userEmailNotFoundError, email)));
    }

	@Override
	public List<UserDTO> findAll() {
		return userRepository.findAll().stream().map(userMapper::toUserDTO).collect(Collectors.toList());
	}

    @Override
    public List<UserDTO> findByCategoryId(Integer categoryId) {
        return userRepository.findByCategoryId(categoryId).stream().map(userMapper::toUserDTO).collect(Collectors.toList());
    }

}
