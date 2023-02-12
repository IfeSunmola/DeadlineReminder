package com.example.backend.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author Ife Sunmola
 */
@ConfigurationProperties(prefix = "app")
@Getter
@ToString
@RequiredArgsConstructor
public class AppProperties {
	private final RSAPublicKey publicKey;
	private final RSAPrivateKey privateKey;
	private final int jwtExpTime;
	private final int jwtExpTimeDefault;
}