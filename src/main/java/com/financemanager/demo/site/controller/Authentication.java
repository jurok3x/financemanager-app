package com.financemanager.demo.site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.financemanager.demo.site.config.jwt.JwtProvider;
import com.financemanager.demo.site.entity.User;
import com.financemanager.demo.site.model.UserModel;
import com.financemanager.demo.site.service.UserModelAssembler;
import com.financemanager.demo.site.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@RestController
@AllArgsConstructor
@Log
public class Authentication {
	
	private final JwtProvider jwtProvider;
	private final UserModelAssembler userAssembler;
	private final UserService userService;
	
	@PostMapping("/api/auth")
	public ResponseEntity<UserModel> login(@RequestBody AuthRequest request){
		try {
		User user = userService.findByLogin(request.getLogin()).get();
		String token = jwtProvider.generateToken(user.getLogin());
		return ResponseEntity.ok()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.body(userAssembler.toModel(user));
		} catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
	}
	
	@GetMapping("/api/auth")
	public String test(@RequestHeader("Content-Type") String token) {
		return token;
	}

}
