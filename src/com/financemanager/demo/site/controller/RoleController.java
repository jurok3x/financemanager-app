package com.financemanager.demo.site.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.financemanager.demo.site.entity.Role;
import com.financemanager.demo.site.exception.ResourceNotFoundException;
import com.financemanager.demo.site.model.RoleModel;
import com.financemanager.demo.site.service.RoleModelAssembler;
import com.financemanager.demo.site.service.RoleService;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
@Log
public class RoleController {

	private final RoleService roleService;
	private final RoleModelAssembler roleAssembler;
	
	@GetMapping
	public ResponseEntity<CollectionModel<RoleModel>> findAllRoles() {
		log.info("Handling find all roles request");
		List<Role> roles = roleService.findAll();
		return new ResponseEntity<>(
				roleAssembler.toCollectionModel(roles),
				HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<RoleModel> findRoleById(@PathVariable 
			@Min(value = 1, message = "Id should be greater than 1") Integer id) throws ResourceNotFoundException {
		log.info("Handling find all roles request");
		return roleService.findById(id)
				.map(roleAssembler::toModel)
				.map(ResponseEntity::ok)
				.orElseThrow(()->new ResourceNotFoundException("Role with ID :"
						+ id +" Not Found!"));
	}
	
	@PostMapping
    public ResponseEntity<?> saveRole(@Valid @RequestBody Role role) {
        log.info("Handling save role: " + role);
        Role addedRole = roleService.saveRole(role);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedRole.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateCategory(@PathVariable
			@Min(value = 1, message = "Id should be greater than 1") Integer id,
			@Valid @RequestBody Role updatedRole){
		log.info("Handling update role with id = " + id);	
		return roleService.findById(id)
				.map(role->{
					role.setId(updatedRole.getId());
					role.setName(updatedRole.getName());
					roleService.saveRole(role);
			        return ResponseEntity.ok().build(); 
				})
				.orElseGet(() -> {
						updatedRole.setId(id);
						Role addedRole =  roleService.saveRole(updatedRole);
						URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				                .path("/{id}")
				                .buildAndExpand(addedRole.getId())
				                .toUri();
				        return ResponseEntity.created(location).build();
				        });
	}
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable
    		@Min(value = 1, message = "Id should be greater than 1") Integer id) throws ResourceNotFoundException{
        log.info("Handling delete role with id = " + id);
        Role role = roleService.findById(id).orElseThrow(
        		()->new ResourceNotFoundException("Role with ID :" + id +" Not Found!"));;
        roleService.deleteRole(role.getId());
        return ResponseEntity.noContent().build();
    }
}
