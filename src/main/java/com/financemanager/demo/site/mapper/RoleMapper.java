package com.financemanager.demo.site.mapper;

import com.financemanager.demo.site.dto.RoleDTO;
import com.financemanager.demo.site.entity.Role;

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
