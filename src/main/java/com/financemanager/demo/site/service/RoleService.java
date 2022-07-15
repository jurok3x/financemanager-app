package com.financemanager.demo.site.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.financemanager.demo.site.dto.RoleDTO;

@Component
public interface RoleService {

	RoleDTO save(RoleDTO role);

    void delete(Integer id);

    List<RoleDTO> findAll();

	Optional<RoleDTO> findById(Integer roleId);

	Optional<RoleDTO> findByName(String name);

}
