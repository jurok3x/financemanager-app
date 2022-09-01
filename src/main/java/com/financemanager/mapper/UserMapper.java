package com.financemanager.mapper;

import com.financemanager.dto.UserDTO;
import com.financemanager.entity.User;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class UserMapper {

    private RoleMapper roleMapper;
    private CategoryMapper categoryMapper;

    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        if (user.getRole() != null) {
            userDTO.setRoleDTO(roleMapper.toRoleDTO(user.getRole()));
        }
        if (user.getCategories().size() != 0) {
            userDTO.setCategories(
                    user.getCategories().stream().map(categoryMapper::toCategoryDTO).collect(Collectors.toSet()));
        }
        return userDTO;
    }

    public User toUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        if (userDTO.getRoleDTO() != null) {
            user.setRole(roleMapper.toRole(userDTO.getRoleDTO()));
        }
        return user;
    }

}
