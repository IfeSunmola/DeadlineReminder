package com.example.backend.controllers;

import com.example.backend.models.Account;
import com.example.backend.security.AuthAccountService;
import com.example.backend.security.jwt.JwtUtils;
import com.example.backend.services.AccountService;
import com.example.backend.transfer_objects.LoginData;
import com.example.backend.transfer_objects.RegisterData;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
	private final JwtUtils jwtUtils;
	private final AuthAccountService authAccountService;
	private final AuthenticationManager authManager;
	private final AccountService accountService;

	@PostMapping("/register")
	public ResponseEntity<Account> createAccount(@RequestBody @Valid RegisterData registerData, BindingResult result) throws MethodArgumentNotValidException {
		accountService.validateData(registerData, result);
		return new ResponseEntity<>(accountService.createAccount(registerData), HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<Account> doLogin(@RequestBody @Valid LoginData loginData) {
		return new ResponseEntity<>(accountService.doLogin(loginData), HttpStatus.OK);
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