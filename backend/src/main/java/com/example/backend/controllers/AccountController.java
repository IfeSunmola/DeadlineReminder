package com.example.backend.controllers;

import com.example.backend.models.Account;
import com.example.backend.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author Ife Sunmola
 */
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class AccountController {
	private final AccountService accountService;

	@GetMapping("/get")
	public Account getAccount(Principal principal) {
		return accountService.findByEmail(principal.getName());
	}
}