package com.example.backend.controllers;

import com.example.backend.services.AccountService;
import com.example.backend.transfer_objects.RegisterData;
import com.example.backend.transfer_objects.VerifyCodeData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@CrossOrigin(origins = "http://localhost:4200/")
public class AuthController {
	private final AccountService accountService;

	@PostMapping("/email-exists")
	public boolean emailExists(@RequestBody String email) {
		return accountService.emailExists(email);
	}

	@PostMapping("/register")
	public Map<String, String> register(@RequestBody @Valid RegisterData registerData, BindingResult result) throws MethodArgumentNotValidException, NoSuchMethodException {
		accountService.validateData(registerData, result); // throws exception if there are errors
		return accountService.createAccount(registerData);
	}

	@PostMapping("/register/verify")
	public String confirmCode(@RequestBody VerifyCodeData data) {
		return accountService.verifyAccount(data);
	}
}