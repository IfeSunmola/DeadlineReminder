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
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

import java.time.Duration;

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
	public AuthenticationProvider authProvider() {
		return new CustomAuthenticationProvider(authAccountService, passwordEncoder());
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
		*
		* Leaving the delay:
		* return NimbusJwtDecoder.withPublicKey(appProps.getPublicKey()).build();
		* */
		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(appProps.getPublicKey()).build();
		OAuth2TokenValidator<Jwt> withClockSkew = new DelegatingOAuth2TokenValidator<>(
				new JwtTimestampValidator(Duration.ofSeconds(0)));

		jwtDecoder.setJwtValidator(withClockSkew);
		return jwtDecoder;
	}

	@Bean
	public JwtEncoder jwtEncoder() {
		JWK jwk = new RSAKey.Builder(appProps.getPublicKey()).privateKey(appProps.getPrivateKey()).build();
		JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwkSource);
	}
}