package com.example.backend.controllers;

import com.example.backend.models.Account;
import com.example.backend.security.AuthAccountService;
import com.example.backend.services.AccountService;
import com.example.backend.transfer_objects.LoginData;
import com.example.backend.transfer_objects.RegisterData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

/**
 * Handles requests to /accounts/auth
 *
 * @author Ife Sunmola
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts/auth")
@Slf4j
public class AuthController {

	private final AuthAccountService authAccountService;

	private final AccountService accountService;

	@PostMapping("/register")
	public ResponseEntity<Account> createAccount(@RequestBody @Valid RegisterData registerData, BindingResult result) throws MethodArgumentNotValidException {
		accountService.validateData(registerData, result);
		return new ResponseEntity<>(accountService.createAccount(registerData), HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public String doLogin(@RequestBody @Valid LoginData loginData) {
		return accountService.generateToken(loginData);
	}

	@PostMapping("/token")
	public String token(Authentication auth) {
		log.debug("Token requested for user: {}", auth.getName());
		String token = accountService.generateToken(auth);
		log.debug("Token granted: {}", token);
		return token;
	}

	@GetMapping("/not-logged-in")
	public String page1() {
		return "<h1>NO AUTHENTICATION NEEDED</h1>";
	}

	@GetMapping("/logged-in")
	public String page2() {
		return "<h1>YOU ARE LOGGED IN</h1>";
	}

}