package com.example.backend.services;

import com.example.backend.models.Account;
import com.example.backend.repos.AccountRepo;
import com.example.backend.security.AppProperties;
import com.example.backend.transfer_objects.RegisterData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
	private final AuthenticationManager authManager;
	private final AppProperties appProps;

	/**
	 * Method to simply check if an email exists in the db.
	 * <p>
	 * Should be used in the front end before sending the entire request body
	 *
	 * @param email the email to check for
	 * @return True if email exits, false if not
	 */
	public boolean emailExists(String email) {
		return accountRepo.existsByEmail(email);
	}

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
	 * Method to authenticate a user and generate a JWT token for them
	 *
	 * @param email    the email of the user
	 * @param password the password of the user
	 * @return the user's jwt token
	 */
	public String authenticateUser(String email, String password, Boolean stayLoggedIn) {
		Authentication auth = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						email,
						password
				)
		);
		SecurityContextHolder.getContext().setAuthentication(auth);

		return generateToken(auth, stayLoggedIn);
	}

	/**
	 * private method to generate a JWT token for a user
	 *
	 * @param auth the authentication object
	 * @return the generated jwt token
	 */
	private String generateToken(Authentication auth, Boolean stayLoggedIn) {
		/*
		 * By default, the tokens will expire 60 seconds past their actual expiration time. This happens to account for clock drift.
		 * See the bean for JwtDecoder for how to remove the delay. HS256 Algorithm also used as default
		 **/
		Instant now = Instant.now();
		String scope = auth.getAuthorities().toString(); // role
		String email = auth.getName();
		int expirationTime = stayLoggedIn ? appProps.jwtExpTime() : appProps.jwtExpTimeDefault();
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("My Issuer ID")
				.issuedAt(now)
				.expiresAt(now.plus(expirationTime, ChronoUnit.DAYS))
				.subject(email)
				.claim("scope", scope)
				.build();
		return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}
}