package com.financemanager.mapper;

import com.financemanager.dto.UserDTO;
import com.financemanager.entity.User;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserMapper {
    
    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public User toUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        return user;
    }

}
