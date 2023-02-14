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
	public ResponseEntity<Map<String, String>> generateToken(@RequestBody @Valid LoginData loginData) {
		log.info("Token requested for user: {}", loginData.getEmail());
		Map<String, String> result =
				accountService.authenticateUser(loginData);

		if (result.containsKey("token")) {
			log.info("Token granted: {}\n", result);
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		log.info("Token not granted: {}\n", result);
		return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
	}

	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> register(@RequestBody @Valid RegisterData registerData, BindingResult result)
			throws MethodArgumentNotValidException, NoSuchMethodException {

		accountService.validateData(registerData, result); // throws exception if there are errors, exception is handled with AuthenticationExceptionHandler
		Map<String, String> emailAndCodeId = accountService.createAccount(registerData);
		return new ResponseEntity<>(emailAndCodeId, HttpStatus.CREATED);
	}

	@PostMapping("/register/verify")
	public String confirmCode(@RequestBody VerifyCodeData data) {
		return accountService.verifyAccount(data);
	}

	@PostMapping("/email-exists")
	public boolean emailExists(@RequestBody String email) {
		return accountService.emailExists(email);
	}

	@PostMapping("/send-verification-email")
	public ResponseEntity<Map<String, String>> sendVerificationEmail(@RequestBody String email) {
		log.info("Verification email requested for user: {}", email);
		Map<String, String> emailAndCodeId = accountService.sendVerificationEmail(accountService.findByEmail(email));
		return new ResponseEntity<>(emailAndCodeId, HttpStatus.CREATED);
	}
}