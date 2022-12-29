package com.example.backend.security;

import com.example.backend.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
	private final AuthAccountService authAccountService;
	private final JwtFilter jwtFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf().disable() //TODO: enable csrf
				.cors().and()
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers(WHITELISTED_URLS).permitAll()
						.anyRequest().authenticated()
				)
				.userDetailsService(authAccountService)
				.sessionManagement((sessionManagement) -> sessionManagement
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.formLogin((form) -> form
						.loginProcessingUrl("/accounts/auth/login") // This is giving me serious issues. Just don't change it
						.permitAll()
				)
				.logout(LogoutConfigurer::permitAll)
		;

		return http.build();
	}
}