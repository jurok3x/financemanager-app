package com.financemanager.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financemanager.dto.UserDTO;
import com.financemanager.exception.ResourceNotFoundException;
import com.financemanager.model.UserModel;
import com.financemanager.service.UserService;
import com.financemanager.service.assembler.UserModelAssembler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@PropertySource(value = { "classpath:/messages/user/info.properties" })
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
	private final UserModelAssembler userAssembler;
	
	@Value("${delete.info}")
	private String deleteInfo;
	@Value("${find_by_category_id.info}")
    private String findByCategoryIdInfo;
	@Value("${find_all.info}")
    private String findAllInfo;
	@Value("${find_by_email.info}")
    private String findByEmailInfo;
	@Value("${find_by_id.info}")
    private String findByIdInfo; 
    @Value("${add_category.info}")
    private String addCategoryInfo;
    @Value("${remove_category.info}")
    private String removeCategoryInfo;
    
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('user:read')")
	public ResponseEntity<UserModel> findById(@PathVariable Integer id) throws ResourceNotFoundException {
		log.info(findByIdInfo, id);
		return ResponseEntity.ok(userAssembler.toModel(userService.findById(id)));
	}
	
	@GetMapping("/{email}")
	@PreAuthorize("hasAuthority('user:read')")
	public ResponseEntity<UserModel> findByEmail(@PathVariable @NotBlank String email) throws ResourceNotFoundException{
		log.info(findByEmailInfo, email);
		return ResponseEntity.ok(userAssembler.toModel(userService.findByEmail(email)));
	}

	@GetMapping
	@PreAuthorize("hasAuthority('user:read')")
	public ResponseEntity<CollectionModel<UserModel>> findAllUsers() {
		log.info(findAllInfo);
		List<UserDTO> users = userService.findAll();
		return new ResponseEntity<>(
				userAssembler.toCollectionModel(users),
				HttpStatus.OK);
	}
	
	@GetMapping("/category/{categoryId}")
	@PreAuthorize("hasAuthority('user:read')") 
    public ResponseEntity<CollectionModel<UserModel>> findUsersByCategoryId(@PathVariable Integer categoryId) {
        log.info(findByCategoryIdInfo, categoryId);
        List<UserDTO> users = userService.findByCategoryId(categoryId);
        return new ResponseEntity<>(
                userAssembler.toCollectionModel(users),
                HttpStatus.OK);
    }
	
	@PostMapping("{userId}/add/category/{categoryId}")
	@PreAuthorize("#userId == authentication.principal.id && hasAuthority('user:write')")
	public ResponseEntity<UserModel> addCategory(@PathVariable Integer userId, @PathVariable Integer categoryId) {
	    log.info(addCategoryInfo, categoryId, userId);
	    return ResponseEntity.ok(userAssembler.toModel(userService.addCategory(userId, categoryId)));
	}
	
	@PostMapping("{userId}/remove/category/{categoryId}")
	@PreAuthorize("#userId == authentication.principal.id && hasAuthority('user:write')")
    public ResponseEntity<UserModel> removeCategory(@PathVariable Integer userId, @PathVariable Integer categoryId) {
        log.info(removeCategoryInfo, categoryId, userId);
        return ResponseEntity.ok(userAssembler.toModel(userService.removeCategory(userId, categoryId)));
    }

	@DeleteMapping("/{id}")
	@PreAuthorize("#id == authentication.principal.id || hasRole('ROLE_ADMIN')") 
	public ResponseEntity<Void> deleteUser(@PathVariable Integer id) throws ResourceNotFoundException{
		log.info(deleteInfo, id);
		userService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
