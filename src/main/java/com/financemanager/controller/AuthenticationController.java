package com.financemanager.controller;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import java.net.URI;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.financemanager.dto.UserDTO;
import com.financemanager.entity.payload.AuthRequest;
import com.financemanager.entity.payload.AuthResponse;
import com.financemanager.entity.payload.SaveUserRequest;
import com.financemanager.exception.UserAlreadyExistsException;
import com.financemanager.model.UserModel;
import com.financemanager.service.AuthService;
import com.financemanager.service.assembler.UserModelAssembler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
public class AuthenticationController {
	
	private static final String LOGIN_INFO = "Login user with email %s";
    private static final String UPDATE_USER_INFO = "Update user with email %s";
    private static final String REGISTRATION_INFO = "Registration new user: %s";
    private final UserModelAssembler userAssembler;
	private final AuthService authService;
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request){
	    log.info(String.format(LOGIN_INFO, request.getEmail()));
		return ResponseEntity.ok(new AuthResponse(authService.login(request), "Bearer"));
	}
	
	@PostMapping("/signup")
    public ResponseEntity<UserModel> registration(@RequestBody @Valid SaveUserRequest request) throws UserAlreadyExistsException{
	    log.info(String.format(REGISTRATION_INFO, request.toString()));
	    UserDTO addedUser = authService.registration(request);
	    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
	
	@PutMapping("/update/{id}")
    public ResponseEntity<UserModel> updateUser(@RequestBody @Valid SaveUserRequest request, @PathVariable Integer id){
	    log.info(String.format(UPDATE_USER_INFO, request.getEmail()));
        return ResponseEntity.ok(userAssembler.toModel(authService.updateUser(request, id)));
    }
	
	@RequestMapping(value = "/" , method = RequestMethod.OPTIONS)
	ResponseEntity<?> singularOptions() 
    {
         return ResponseEntity
                 .ok()
                 .allow(HttpMethod.POST, HttpMethod.OPTIONS)
                 .build();
    }

}
