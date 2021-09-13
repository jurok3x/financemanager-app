package com.financemanager.demo.site.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.financemanager.demo.site.entity.Role;
import com.financemanager.demo.site.exception.ResourceNotFoundException;
import com.financemanager.demo.site.repository.RoleRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DefaultRoleService implements RoleService {

	private final RoleRepository roleRepository;

	@Override
	public Role saveRole(Role role) {
		return roleRepository.save(role);

	}

	@Override
	public Optional<Role> findById(Integer id) throws ResourceNotFoundException{
		return roleRepository.findById(id);
	}

	@Override
	public void deleteRole(Integer roleId) {
		roleRepository.deleteById(roleId);
	}

	@Override
	public List<Role> findAll() {
		return roleRepository.findAll().stream().collect(Collectors.toList());
	}

	@Override
	public Optional<Role> findByName(String name) {
		return roleRepository.findByName(name);
	}

}
