package com.financemanager.mapper;

import com.financemanager.dto.RoleDTO;
import com.financemanager.entity.Role;

import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    
    public RoleDTO toRoleDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        return roleDTO;
    }
    
    public Role toRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setName(roleDTO.getName());
        return role;
    }

}
