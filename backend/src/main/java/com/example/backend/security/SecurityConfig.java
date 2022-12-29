package com.example.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author Ife Sunmola
 */
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private static final String BASE_AUTH_URL = "/accounts/auth";
	private static final String[] WHITELISTED_URLS = {
			BASE_AUTH_URL + "/login",
			BASE_AUTH_URL + "/register",
			BASE_AUTH_URL + "/not-logged-in",
	};

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable) //TODO: enable csrf
				.cors(AbstractHttpConfigurer::disable) //TODO: enable cors
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers(WHITELISTED_URLS).permitAll()
						.anyRequest().authenticated()
				)
				.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
				.sessionManagement((session) -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.httpBasic(withDefaults())
				.logout(LogoutConfigurer::permitAll);
		return http.build();
	}
}