package com.example.backend.controllers;

import com.example.backend.services.AccountService;
import com.example.backend.services.AuthService;
import com.example.backend.transfer_objects.LoginData;
import com.example.backend.transfer_objects.PasswordResetData;
import com.example.backend.transfer_objects.RegisterData;
import com.example.backend.transfer_objects.VerifyCodeData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Ife Sunmola
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;
	private final AccountService accountService;

	@PostMapping(value = "/generate-token")
	public ResponseEntity<Map<String, String>> generateToken(@RequestBody @Valid LoginData loginData) {
		log.info("Token requested for user: {}", loginData.getEmail());
		Map<String, String> result =
				authService.authenticateUser(loginData);

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

		authService.validateData(registerData, result); // throws exception if there are errors
		Map<String, String> emailAndCodeId = authService.createAccount(registerData);
		return new ResponseEntity<>(emailAndCodeId, HttpStatus.CREATED);
	}

	@PostMapping("/register/verify")
	public Map<String, String> confirmCode(@RequestBody VerifyCodeData data) {
		return authService.verifyAccount(data);
	}

	@PostMapping("/email-exists")
	public boolean emailExists(@RequestBody String email) {
		return authService.emailExists(email);
	}

	@PostMapping("/send-verification-email")
	public ResponseEntity<Map<String, String>> sendVerificationEmail(@RequestBody String email) {
		log.info("Verification email requested for user: {}", email);
		Map<String, String> emailAndCodeId = authService.sendVerificationEmail(accountService.findByEmail(email));
		return new ResponseEntity<>(emailAndCodeId, HttpStatus.CREATED);
	}

	@PostMapping("/send-reset-password-email")
	public HttpStatus sendResetPasswordEmail(@RequestBody String email) {
		log.info("Reset password email requested for user: {}", email);
		authService.sendResetPasswordEmail(email);
		return HttpStatus.CREATED;
	}

	@GetMapping("/reset-password/verify")
	public String verifyPasswordResetCode(@RequestParam String token) {
		log.info("Password reset code verification requested for code: {}", token);
		return authService.verifyPasswordResetToken(token); // returns the email associated with the code
	}

	@PutMapping("/confirm-reset")
	public Map<String, String> confirmPasswordReset(@RequestBody @Valid PasswordResetData passwordResetData, BindingResult result)
			throws MethodArgumentNotValidException, NoSuchMethodException {
		log.info("Reset password requested for user: {}", passwordResetData);
		authService.validatePasswordResetData(passwordResetData, result);  // throws exception if there are errors
		return authService.confirmPasswordReset(passwordResetData);
	}
}