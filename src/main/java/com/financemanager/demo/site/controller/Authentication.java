package com.financemanager.demo.site.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.financemanager.demo.site.config.jwt.JwtProvider;
import com.financemanager.demo.site.entity.User;
import com.financemanager.demo.site.model.UserModel;
import com.financemanager.demo.site.service.UserModelAssembler;
import com.financemanager.demo.site.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Log
public class Authentication {
	
	private final JwtProvider jwtProvider;
	private final UserModelAssembler userAssembler;
	private final UserService userService;
	
	@PostMapping
	public ResponseEntity<UserModel> login(@RequestBody AuthRequest request){
		try {
		User user = userService.findByLogin(request.getLogin()).get();
		String token = jwtProvider.generateToken(user.getLogin());
		log.info("Authenticate user " + request.getLogin());
		return ResponseEntity.ok()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.body(userAssembler.toModel(user));
		} catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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
