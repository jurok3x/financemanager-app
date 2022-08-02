package com.financemanager.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.financemanager.dto.UserDTO;
import com.financemanager.exception.AddCategoryException;
import com.financemanager.exception.ResourceNotFoundException;
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
	@Value("${user_category_not_found.error")
	private String userCategoryNotFound;
	@Value("${user_already_has_category.error}")
    private String userAlreadyHasCategory;
	
	
	@Override
	public UserDTO findById(Integer id) {
	    return userRepository.findById(id).map(userMapper::toUserDTO).orElseThrow(() -> new ResourceNotFoundException(
                String.format(userIdNotFoundError, id)));
	}

	@Override
	public void delete(Integer id) {
	    UserDTO userDTO = userRepository.findById(id).map(userMapper::toUserDTO).orElseThrow(() -> new ResourceNotFoundException(
                String.format(userIdNotFoundError, id)));
		userRepository.deleteById(userDTO.getId());		
	}

	@Override
    public UserDTO findByEmail(String email){
	    return userRepository.findByEmail(email).map(userMapper::toUserDTO).orElseThrow(() -> new ResourceNotFoundException(
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

    @Override
    public UserDTO addCategory(Integer userId, Integer categoryId){
        UserDTO userDTO = findById(userId);
        if(userDTO.getCategories().stream().anyMatch(category -> category.getId() == categoryId)) {
            throw new AddCategoryException(String.format(userAlreadyHasCategory, userId, categoryId));
        }
        userRepository.addCategory(userId, categoryId);
        return findById(userId);
    }

    @Override
    public UserDTO removeCategory(Integer userId, Integer categoryId) {
        UserDTO userDTO = findById(userId);
        if(!userDTO.getCategories().stream().anyMatch(category -> category.getId() == categoryId)) {
            throw new ResourceNotFoundException(String.format(userCategoryNotFound, categoryId, userId));
        }
        userRepository.removeCategory(userId, categoryId);
        return findById(userId);
    }

}
