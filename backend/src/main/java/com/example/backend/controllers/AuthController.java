package com.example.backend.controllers;

import com.example.backend.models.Account;
import com.example.backend.services.AccountService;
import com.example.backend.transfer_objects.LoginData;
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
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
	private final AccountService accountService;

	/**
	 * Handles requests to /auth/register
	 *
	 * @param registerData Object containing the user's registration information
	 * @return The newly registered user with a 201 status code  //TODO: Shouldn't return the entire object user
	 * @throws MethodArgumentNotValidException If the user's registration information is invalid
	 */
	@PostMapping("/register")
	public ResponseEntity<Account> createAccount(@RequestBody @Valid RegisterData registerData,
	                                             BindingResult result) throws MethodArgumentNotValidException {
		accountService.validateData(registerData, result); // throws exception if there are errors
		return new ResponseEntity<>(accountService.createAccount(registerData), HttpStatus.CREATED);
	}

	/**
	 * Handles requests to /auth/login. An AuthenticationException is thrown from authenticateUser() if the user's credentials
	 * are invalid or the account is disabled
	 *
	 * @param loginData Object containing the user's login information
	 * @return The user's JWT token with a 200 status code
	 */
	@PostMapping(value = "/generate-token")
	public ResponseEntity<String> token(@RequestBody @Valid LoginData loginData) {
		log.info("Token requested for user: {}", loginData.getEmail());

		String token = accountService.authenticateUser(loginData.getEmail(), loginData.getPassword(), loginData.getStayLoggedIn());

		log.info("Token granted: {}\n", token);
		return new ResponseEntity<>(token, HttpStatus.OK);
	}


	@PostMapping("/email-exists")
	// Just found out these methods return status OK by default.
	// Todo: Remove the response entity if i'm not changing the status code
	public boolean emailExists(@RequestBody String email) {
		return accountService.emailExists(email);
	}

	@GetMapping("/get-user")
	@PreAuthorize("isAuthenticated()")
	public String getLoggedInUserEmail(Authentication auth) {
		return auth.getName();
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