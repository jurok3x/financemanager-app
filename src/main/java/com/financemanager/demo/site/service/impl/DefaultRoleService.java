package com.financemanager.demo.site.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.financemanager.demo.site.dto.RoleDTO;
import com.financemanager.demo.site.exception.ResourceNotFoundException;
import com.financemanager.demo.site.mapper.RoleMapper;
import com.financemanager.demo.site.repository.RoleRepository;
import com.financemanager.demo.site.service.RoleService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DefaultRoleService implements RoleService {

	private static final String ROLE_NAME_NOT_FOUND_ERROR = "Role with name %s not found";
    private static final String ROLE_ID_NOT_FOUND_ERROR = "Role with id %d, not found";
    private final RoleRepository roleRepository;
	private final RoleMapper roleMapper;

	@Override
	public RoleDTO save(RoleDTO roleDTO) {
		return roleMapper.toRoleDTO(roleRepository.save(roleMapper.toRole(roleDTO)));

	}

	@Override
	public Optional<RoleDTO> findById(Integer id) throws ResourceNotFoundException{
		RoleDTO roleDTO = roleRepository.findById(id).map(roleMapper::toRoleDTO).orElseThrow(() -> new ResourceNotFoundException(
                String.format(ROLE_ID_NOT_FOUND_ERROR, id)));
	    return Optional.ofNullable(roleDTO);
	}

	@Override
	public void delete(Integer roleId) {
		roleRepository.deleteById(roleId);
	}

	@Override
	public List<RoleDTO> findAll() {
		return roleRepository.findAll().stream().map(roleMapper::toRoleDTO).collect(Collectors.toList());
	}

	@Override
	public Optional<RoleDTO> findByName(String name) {
	    RoleDTO roleDTO = roleRepository.findByName(name).map(roleMapper::toRoleDTO).orElseThrow(() -> new ResourceNotFoundException(
                String.format(ROLE_NAME_NOT_FOUND_ERROR, name)));
        return Optional.ofNullable(roleDTO);
	}

}
