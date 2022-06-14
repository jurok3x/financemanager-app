package com.financemanager.demo.site.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.validation.constraints.Min;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.financemanager.demo.site.dto.RoleDTO;
import com.financemanager.demo.site.exception.ResourceNotFoundException;
import com.financemanager.demo.site.model.RoleModel;
import com.financemanager.demo.site.service.RoleService;
import com.financemanager.demo.site.service.assembler.RoleModelAssembler;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
@Log
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

	private static final String ROLE_ID_NOT_FOUND_ERROR = "Role with ID %d not Found!";
    private static final String INCORRECT_ID = "Id should be greater than 1";
    private static final String FIND_ALL_ROLES_INFO = "Handling find all roles request";
    private final RoleService roleService;
	private final RoleModelAssembler roleAssembler;
	
	@GetMapping
	public ResponseEntity<CollectionModel<RoleModel>> findAllRoles() {
		log.info(FIND_ALL_ROLES_INFO);
		List<RoleDTO> roles = roleService.findAll();
		return new ResponseEntity<>(
				roleAssembler.toCollectionModel(roles),
				HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<RoleModel> findRoleById(@PathVariable 
			@Min(value = 1, message = INCORRECT_ID) Integer id) throws ResourceNotFoundException {
		log.info(FIND_ALL_ROLES_INFO);
		return roleService.findById(id)
				.map(roleAssembler::toModel)
				.map(ResponseEntity::ok)
				.orElseThrow(()->new ResourceNotFoundException(String.format(ROLE_ID_NOT_FOUND_ERROR, id)));
	}
	
	@RequestMapping(value = "/" , method = RequestMethod.OPTIONS)
	ResponseEntity<?> collectionOptions() 
    {
         return ResponseEntity
                 .ok()
                 .allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS)
                 .build();
    }
	
}
