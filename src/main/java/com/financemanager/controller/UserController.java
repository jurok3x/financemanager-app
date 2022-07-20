package com.financemanager.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financemanager.dto.UserDTO;
import com.financemanager.exception.ResourceNotFoundException;
import com.financemanager.model.UserModel;
import com.financemanager.service.UserService;
import com.financemanager.service.assembler.UserModelAssembler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private static final String DELETE_USER_BY_ID_INFO = "Delete user with id %d";
    private static final String FIND_BY_CATEGORY_ID = "Find users for category with id %d";
    private static final String FIND_ALL_INFO = "Handling find all users request";
    private static final String FIND_BY_EMAIL_INFO = "Handling find with email %s";
    private static final String INCORRECT_ID = "Id should be greater than 1";
    private static final String FIND_BY_ID_INFO = "Handling find user with Id %d";
    private final UserService userService;
	private final UserModelAssembler userAssembler;
	
	@GetMapping("/{id}")
	public ResponseEntity<UserModel> findById(@PathVariable @Min(value = 1,
	message = INCORRECT_ID) Integer id) throws ResourceNotFoundException {
		log.info(String.format(FIND_BY_ID_INFO, id));
		return ResponseEntity.ok(userAssembler.toModel(userService.findById(id)));
	}
	
	@GetMapping("/{email}")
	public ResponseEntity<UserModel> findByEmail(@PathVariable @NotBlank String email) throws ResourceNotFoundException{
		log.info(String.format(FIND_BY_EMAIL_INFO, email));
		return ResponseEntity.ok(userAssembler.toModel(userService.findByEmail(email)));
	}

	@GetMapping
	public ResponseEntity<CollectionModel<UserModel>> findAllUsers() {
		log.info(FIND_ALL_INFO);
		List<UserDTO> users = userService.findAll();
		return new ResponseEntity<>(
				userAssembler.toCollectionModel(users),
				HttpStatus.OK);
	}
	
	@GetMapping("/category/{categoryId}")
    public ResponseEntity<CollectionModel<UserModel>> findUsersByCategoryId(@PathVariable Integer categoryId) {
        log.info(FIND_BY_CATEGORY_ID, categoryId);
        List<UserDTO> users = userService.findByCategoryId(categoryId);
        return new ResponseEntity<>(
                userAssembler.toCollectionModel(users),
                HttpStatus.OK);
    }

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable @Min(value = 1,
			message = INCORRECT_ID) Integer id) throws ResourceNotFoundException{
		log.info(DELETE_USER_BY_ID_INFO, id);
		userService.delete(id);
		return ResponseEntity.noContent().build();
	}
}