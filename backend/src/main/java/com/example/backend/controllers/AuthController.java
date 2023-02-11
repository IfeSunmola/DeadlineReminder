package com.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ife Sunmola
 */
@RestController
@RequiredArgsConstructor
public class AuthController {
	@GetMapping("/auth")
	public String authNeeded() {
		return "YAY! AUTHENTICATED!";
	}

	@GetMapping("/no-auth")
	public String noAuthNeeded() {
		return "NOT AUTHENTICATED!";
	}
}