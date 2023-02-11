package com.example.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author Ife Sunmola
 */
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private static final String[] WHITELISTED_URLS = {
			"/no-auth",
	};

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
//				.headers().frameOptions().disable().and()
//				.csrf(AbstractHttpConfigurer::disable) //TODO: enable csrf
//				.cors(AbstractHttpConfigurer::disable) //TODO: enable cors
//				.cors().and()
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers(new AntPathRequestMatcher("/h2/**")).permitAll()
						.requestMatchers(WHITELISTED_URLS).permitAll()
						.anyRequest().authenticated()
				)
				.oauth2Login().and()
				.sessionManagement((session) -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				);
		return http.build();
	}
}