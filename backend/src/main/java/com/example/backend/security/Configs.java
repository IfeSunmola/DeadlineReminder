package com.example.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Contains all security related beans
 *
 * @author Ife Sunmola
 */
@Configuration
@RequiredArgsConstructor
public class Configs {
	private final AuthAccountService authAccountService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(authAccountService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
}