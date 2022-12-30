package com.example.backend.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * This class contains App information that is read from application.properties
 *
 * @author Ife Sunmola
 */
@ConfigurationProperties(prefix = "app")
public record AppProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey, int jwtExpTime, int jwtExpTimeDefault) {
}
