package com.financemanager.demo.site.mapper;

import com.financemanager.demo.site.dto.UserDTO;
import com.financemanager.demo.site.entity.User;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserMapper {
    
    private RoleMapper roleMapper;
    
    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(roleMapper.toRoleDTO(user.getRole()));
        return userDTO;
    }

    public User toUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setRole(roleMapper.toRole(userDTO.getRole()));
        return user;
    }

}
