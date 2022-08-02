package com.financemanager.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@PropertySource(value = { "classpath:/messages/authentification/info.properties" })
@SecurityRequirement(name = "bearerAuth")
public class AuthenticationController {
	
    private final UserModelAssembler userAssembler;
	private final AuthService authService;
	
	@Value("${login.info}")
    private String loginInfo;
    @Value("${update_user.info}")
    private String updateUserInfo;
    @Value("${registration.info}")
    private String registrationInfo;
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request){
	    log.info(loginInfo, request.getEmail());
		return ResponseEntity.ok(authService.login(request));
	}
	
	@PostMapping("/signup")
    public ResponseEntity<UserModel> registration(@RequestBody @Valid SaveUserRequest request) throws UserAlreadyExistsException{
	    log.info(registrationInfo, request.toString());
	    UserDTO addedUser = authService.registration(request);
	    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
	
	@PutMapping("/update/{id}")
	@PreAuthorize("#id == authentication.principal.id || hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserModel> updateUserName(@RequestBody String userName, @PathVariable Integer id){
	    log.info(updateUserInfo, id, userName);
        return ResponseEntity.ok(userAssembler.toModel(authService.updateName(userName, id)));
    }
	
}
