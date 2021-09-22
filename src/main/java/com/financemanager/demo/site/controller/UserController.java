package com.financemanager.demo.site.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

import com.financemanager.demo.site.entity.User;
import com.financemanager.demo.site.exception.ResourceNotFoundException;
import com.financemanager.demo.site.model.UserModel;
import com.financemanager.demo.site.service.UserModelAssembler;
import com.financemanager.demo.site.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Log
public class UserController {

	private final UserService userService;
	private final UserModelAssembler userAssembler;
	
	@GetMapping("/{id}")
	public ResponseEntity<UserModel> findById(@PathVariable @Min(value = 1,
	message = "Id should be greater than 1") Integer id) throws ResourceNotFoundException {
		log.info("Handling find with Id = " + id);
		return userService.findById(id)
				.map(userAssembler::toModel)
				.map(ResponseEntity::ok)
				.orElseThrow(
						()->new ResourceNotFoundException("User with ID :" + id +" Not Found!"));	
	}
	
	@GetMapping("/{login}")
	public ResponseEntity<UserModel> findByLogin(@PathVariable @NotNull String login) throws ResourceNotFoundException{
		log.info("Handling find with login = " + login);
		return userService.findByLogin(login)
				.map(userAssembler::toModel)
				.map(ResponseEntity::ok)
				.orElseThrow(
						()->new ResourceNotFoundException("User with Login :" + login +" Not Found!"));
	}

	@GetMapping
	public ResponseEntity<CollectionModel<UserModel>> findAllUsers() {
		log.info("Handling find all users request");
		List<User> users = userService.findAll();
		return new ResponseEntity<>(
				userAssembler.toCollectionModel(users),
				HttpStatus.OK);
	}
	
	@GetMapping("/role/{id}")
	public ResponseEntity<CollectionModel<UserModel>> findByRoleId(@PathVariable @Min(value = 1,
		message = "Id should be greater than 1") Integer id) {
		log.info("Handling find all users with role = " + id + " request");
		List<User> users = userService.findByRoleId(id);
		return new ResponseEntity<>(
				userAssembler.toCollectionModel(users),
				HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<?> saveUser(@Valid @RequestBody User user) {
		log.info("Handling save user: " + user);
		User addedUser = userService.saveUser(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(addedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable
			@Min(value = 1, message = "Id should be greater than 1") Integer id,
			@Valid @RequestBody User updatedUser){
		log.info("Handling update user with id = " + id);	
		return userService.findById(id)
				.map(user->{
					user.setId(updatedUser.getId());
					user.setName(updatedUser.getName());
					user.setEmail(updatedUser.getEmail());
					user.setLogin(updatedUser.getLogin());
					user.setPassword(updatedUser.getPassword());
					user.setRole(updatedUser.getRole());
					userService.saveUser(user);
			        return ResponseEntity.ok().build(); 
				})
				.orElseGet(() -> {
						updatedUser.setId(id);
						User addedUser =  userService.saveUser(updatedUser);
						URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				                .path("/{id}")
				                .buildAndExpand(addedUser.getId())
				                .toUri();
				        return ResponseEntity.created(location).build();
				        });
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable @Min(value = 1,
			message = "Id should be greater than 1") Integer id) throws ResourceNotFoundException{
		log.info("Handling delete user request with id = " + id);
		User deletedUser = userService.findById(id).orElseThrow(
				()->new ResourceNotFoundException("User with ID :" + id +" Not Found!"));
		userService.deleteUser(deletedUser.getId());
		return ResponseEntity.noContent().build();
	}

}
