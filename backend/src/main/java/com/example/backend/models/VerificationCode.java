package com.example.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.UUID;

import static com.example.backend.models.VerificationCodeType.EMAIL_VERIFICATION;

/**
 * @author Ife Sunmola
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class VerificationCode {
	private static final int CODE_LENGTH = 5;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long codeId;

	@ManyToOne @JoinColumn(name = "owner", nullable = false)
	private Account owner;

	private String code;
	private Instant dateCreated;
	private Instant expiryDate;

	public VerificationCode(Account owner, VerificationCodeType type) {
		this.owner = owner;
		dateCreated = Instant.now();
		expiryDate = dateCreated.plus(30, ChronoUnit.MINUTES);
		if (type == EMAIL_VERIFICATION) {
			this.code = generateCode();
		}
		else if (type == VerificationCodeType.PASSWORD_RESET) {
			this.code = UUID.randomUUID().toString();
		}
		else { //TODO: Handle exceptions elegantly with global exception handler
			throw new IllegalArgumentException("Invalid VerificationCodeType");
		}
	}

	private static String generateCode() {
		StringBuilder sb = new StringBuilder();
		var chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		Random random = new Random();
		for (int i = 0; i < CODE_LENGTH; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}

	public boolean isExpired() {
		return Instant.now().isAfter(expiryDate);
	}
}