package com.example.backend.services;

import com.example.backend.models.Account;
import com.example.backend.repos.AccountRepo;
import com.example.backend.transfer_objects.RegisterData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * Service class.
 * This class holds the important parts of doing anything related to a user's account
 *
 * @author Ife Sunmola
 */
@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountRepo accountRepo;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtEncoder encoder;

	/**
	 * Method to validate the registration data gotten from the front end
	 *
	 * @param registerData the registration data
	 * @param result       contains all the errors gotten by the @Valid annotation
	 * @throws MethodArgumentNotValidException if there are any errors in the registration data. The exception is handled by ExceptionHandlerAdvice
	 */
	public void validateData(RegisterData registerData, BindingResult result) throws MethodArgumentNotValidException {
		if (!registerData.getPassword().equals(registerData.getConfirmPassword())) {
			result.rejectValue("confirmPassword", "", "Passwords do not match");
		}
		if (accountRepo.existsByEmail(registerData.getEmail())) {
			result.rejectValue("email", "", "Email " + registerData.getEmail() + " is already in use");
		}
		if (result.hasFieldErrors()) {
			throw new MethodArgumentNotValidException(null, result);// TODO: Shouldn't pass null here
		}
	}

	/**
	 * Method to create a new account. This method assumes the data it receives is 100% valid
	 *
	 * @param registerData the registration data
	 * @return the newly created account
	 */
	public Account createAccount(RegisterData registerData) {
		Account account = new Account(
				registerData.getEmail().toLowerCase(Locale.ROOT),
				passwordEncoder.encode(registerData.getPassword()),
				true, //TODO: Email verification
				registerData.isAcceptedTerms(), // should always be true
				Instant.now()
		);
		return accountRepo.save(account);
	}

	/**
	 * Method to generate a JWT token for a user
	 *
	 * @param auth the authentication object
	 * @return the generated jwt token
	 */
	public String generateToken(Authentication auth) {
		// By default, the tokens will expire 60 seconds past their actual expiration time. This happens to account for clock drift.
		// See the bean for JwtDecoder for how to remove the delay
		// HS256 Algorithm also used as default
		Instant now = Instant.now();
		String scope = auth.getAuthorities().toString(); // role
		String email = auth.getName();
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("My Issuer ID")
				.issuedAt(now)
				.expiresAt(now.plus(1, ChronoUnit.MINUTES))
				.subject(email)
				.claim("scope", scope)
				.build();
		return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}
}