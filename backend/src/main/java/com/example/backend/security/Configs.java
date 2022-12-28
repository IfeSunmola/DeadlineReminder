package com.example.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean //TODO: Remove before prod
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {

				registry.addMapping("/**")
						.allowedOrigins("*")
						.allowedMethods("*")
						.allowedHeaders("*");

			}
		};
	}
}