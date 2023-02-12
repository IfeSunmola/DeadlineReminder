package com.example.backend.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

/**
 * Contains all security related beans
 *
 * @author Ife Sunmola
 */
@Configuration
@RequiredArgsConstructor
public class Configs {
	private final AuthAccountService authAccountService;
	private final AppProperties appProps;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}


	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(authAccountService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		/*
		* https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html#oauth2resourceserver-jwt-validation-clockskew
		* Removing the delay:
		* NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaProperties.publicKey()).build();
			OAuth2TokenValidator<Jwt> withClockSkew = new DelegatingOAuth2TokenValidator<>(
										new JwtTimestampValidator(Duration.ofSeconds(0)));

		* jwtDecoder.setJwtValidator(withClockSkew);
		* return jwtDecoder;
		* */
		return NimbusJwtDecoder.withPublicKey(appProps.getPublicKey()).build();
	}

	@Bean
	public JwtEncoder jwtEncoder() {
		JWK jwk = new RSAKey.Builder(appProps.getPublicKey()).privateKey(appProps.getPrivateKey()).build();
		JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwkSource);
	}
}