package com.example.backend.services;

import com.example.backend.models.Account;
import com.example.backend.repos.AccountRepo;
import com.example.backend.security.AuthAccount;
import com.example.backend.security.AuthAccountService;
import com.example.backend.transfer_objects.LoginData;
import com.example.backend.transfer_objects.RegisterData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
	private final AuthenticationManager authManager;
	private final AuthAccountService authAccountService;
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
				registerData.getEmail(),
				passwordEncoder.encode(registerData.getPassword()),
				true, //TODO: Email verification
				registerData.isAcceptedTerms(), // should always be true
				Instant.now()
		);
		return accountRepo.save(account);
	}

	public Account doLogin(LoginData loginData) {
		System.out.println("LoginData: " + loginData);
		return accountRepo.findByEmail(loginData.getEmail()).orElse(null);
	}

	public String generateToken(LoginData loginData) {
		authManager.authenticate( // throws AuthenticationException if authentication fails. Handled by ExceptionHandlerAdvice
				new UsernamePasswordAuthenticationToken(loginData.getEmail(), loginData.getPassword()));

		AuthAccount account = authAccountService.loadUserByUsername(loginData.getEmail());
		return null;
	}

	public String generateToken(Authentication authentication) {
		Instant now = Instant.now();
		String scope = authentication.getAuthorities().toString();
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("self")
				.issuedAt(now)
				.expiresAt(now.plus(1, ChronoUnit.HOURS))
				.subject(authentication.getName())
				.claim("scope", scope)
				.build();
		return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}
}