package com.example.backend.controllers;

import com.example.backend.models.Account;
import com.example.backend.services.AccountService;
import com.example.backend.transfer_objects.RegisterData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

/**
 * Handles requests to /auth
 *
 * @author Ife Sunmola
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {
	private final AccountService accountService;

	@PostMapping("/register")
	public ResponseEntity<Account> createAccount(@RequestBody @Valid RegisterData registerData, BindingResult result) throws MethodArgumentNotValidException {
		accountService.validateData(registerData, result);
		return new ResponseEntity<>(accountService.createAccount(registerData), HttpStatus.CREATED);
	}

	@PostMapping("/generate-token")
	public String token(Authentication auth) {
		/*
		 * For some reason, requests directed to this end point don't actually get here if the user info is wrong.
		 * Add a logger to print the auth object/name, and you'll notice that if the user info is wrong, the logger
		 * won't print anything. Why is this happening?
		 *
		 * I think it has to do Authentication being passed as an argument.
		 * */
		log.info("Token requested for user: {}", auth.getName());
		String token = accountService.generateToken(auth);
		log.info("Token granted: {}\n", token);
		return token;
	}

	@GetMapping("/not-logged-in")
	public String page1() {
		return "<h1>NO AUTHENTICATION NEEDED</h1>";
	}


	@GetMapping("/logged-in")
	@PreAuthorize("isAuthenticated()")
	public String page2() {
		return "<h1>YOU ARE LOGGED IN</h1>";
	}

}