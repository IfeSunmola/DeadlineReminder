package com.example.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Ife Sunmola
 * @see <a href="https://github.com/spring-projects/spring-security-samples/tree/main/servlet/spring-boot/java/jwt/login">From Spring Security</a>
 */
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private static final String BASE_AUTH_URL = "/auth";
	private static final String[] WHITELISTED_URLS = {
			BASE_AUTH_URL + "/generate-token",
			BASE_AUTH_URL + "/register",
			BASE_AUTH_URL + "/not-logged-in",
			BASE_AUTH_URL + "/email-exists"
	};

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable) //TODO: enable csrf
				.cors(AbstractHttpConfigurer::disable) //TODO: enable cors
				.cors().and()
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers(WHITELISTED_URLS).permitAll()
						.anyRequest().authenticated()
				)
				.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
				.sessionManagement((session) -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				);
		return http.build();
	}

//	@Bean
//	public CorsRegistration addCorsMappings() {
//		return new CorsRegistry().addMapping("/**")
//				.allowedOrigins("http://localhost:4200")
//				.allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
//				.allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers")
//				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
//				.allowCredentials(true).maxAge(3600);
//	}
}