package com.financemanager.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    private static final String FIND_BY_CATEGORY_ID = "Find users for category with id %d";
    private static final String FIND_ALL_INFO = "Handling find all users request";
    private static final String USER_EMAIL_NOT_FOUND_ERROR = "User with email %s Not Found!";
    private static final String FIND_BY_EMAIL_INFO = "Handling find with email %s";
    private static final String USER_ID_NOT_FOUND_ERROR = "User with id %d Not Found!";
    private static final String INCORRECT_ID = "Id should be greater than 1";
    private static final String FIND_BY_ID_INFO = "Handling find user with Id %d";
    private final UserService userService;
	private final UserModelAssembler userAssembler;
	
	@GetMapping("/{id}")
	public ResponseEntity<UserModel> findById(@PathVariable @Min(value = 1,
	message = INCORRECT_ID) Integer id) throws ResourceNotFoundException {
		log.info(String.format(FIND_BY_ID_INFO, id));
		return userService.findById(id)
				.map(userAssembler::toModel)
				.map(ResponseEntity::ok)
				.orElseThrow(
						()->new ResourceNotFoundException(String.format(USER_ID_NOT_FOUND_ERROR, id)));	
	}
	
	@GetMapping("/{email}")
	public ResponseEntity<UserModel> findByEmail(@PathVariable @NotBlank String email) throws ResourceNotFoundException{
		log.info(String.format(FIND_BY_EMAIL_INFO, email));
		return userService.findByEmail(email)
				.map(userAssembler::toModel)
				.map(ResponseEntity::ok)
				.orElseThrow(
						()->new ResourceNotFoundException(String.format(USER_EMAIL_NOT_FOUND_ERROR, email)));
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
        log.info(String.format(FIND_BY_CATEGORY_ID, categoryId));
        List<UserDTO> users = userService.findByCategoryId(categoryId);
        return new ResponseEntity<>(
                userAssembler.toCollectionModel(users),
                HttpStatus.OK);
    }

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable @Min(value = 1,
			message = INCORRECT_ID) Integer id) throws ResourceNotFoundException{
		log.info(String.format(FIND_BY_ID_INFO, id));
		UserDTO deletedUser = userService.findById(id).orElseThrow(
				()->new ResourceNotFoundException(String.format(USER_ID_NOT_FOUND_ERROR, id)));
		userService.delete(deletedUser.getId());
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/" , method = RequestMethod.OPTIONS)
	ResponseEntity<?> collectionOptions() 
    {
         return ResponseEntity
                 .ok()
                 .allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS)
                 .build();
    }
	
	@RequestMapping(value = "/{id}" , method = RequestMethod.OPTIONS)
	ResponseEntity<?> singularOptions() 
    {
         return ResponseEntity
                 .ok()
                 .allow(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE , HttpMethod.OPTIONS)
                 .build();
    }
	
	@RequestMapping(value = "/{login}, /role/{id}" , method = RequestMethod.OPTIONS)
	ResponseEntity<?> specialOptions() 
    {
         return ResponseEntity
                 .ok()
                 .allow(HttpMethod.GET, HttpMethod.OPTIONS)
                 .build();
    }

}
