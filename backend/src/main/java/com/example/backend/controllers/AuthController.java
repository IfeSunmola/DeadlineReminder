package com.example.backend.controllers;

import com.example.backend.services.AccountService;
import com.example.backend.transfer_objects.LoginData;
import com.example.backend.transfer_objects.RegisterData;
import com.example.backend.transfer_objects.VerifyCodeData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Ife Sunmola
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	private final AccountService accountService;

	@PostMapping(value = "/generate-token")
	public ResponseEntity<String> token(@RequestBody @Valid LoginData loginData) {
		log.info("Token requested for user: {}", loginData.getEmail());

		String token = accountService.authenticateUser(loginData.getEmail(), loginData.getPassword(), loginData.getStayLoggedIn());

		log.info("Token granted: {}\n", token);
		return new ResponseEntity<>(token, HttpStatus.OK);
	}

	@PostMapping("/register")
	public Map<String, String> register(@RequestBody @Valid RegisterData registerData, BindingResult result)
			throws MethodArgumentNotValidException, NoSuchMethodException {
		// throws exception if there are errors, exception is handled with AuthenticationExceptionHandler
		accountService.validateData(registerData, result);
		return accountService.createAccount(registerData);
	}

	@PostMapping("/register/verify")
	public String confirmCode(@RequestBody VerifyCodeData data) {
		return accountService.verifyAccount(data);
	}

	@PostMapping("/email-exists")
	public boolean emailExists(@RequestBody String email) {
		return accountService.emailExists(email);
	}
}