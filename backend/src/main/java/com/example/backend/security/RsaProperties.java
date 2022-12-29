package com.example.backend.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author Ife Sunmola
 */
@ConfigurationProperties(prefix = "rsa")
public record RsaProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
