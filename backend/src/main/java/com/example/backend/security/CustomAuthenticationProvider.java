package com.example.backend.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Ife Sunmola
 * <p>
 * Reason for providing my own implementatoin of AuthenticationProvider (so I don't forget):
 * By default, for some weird reason, if someone tries logging in with an incorrect password, AND the account is disabled,
 * spring security would say account disabled.
 * <p>
 * To explain further, If someone is trying to log into my account with a wrong password, and my account is disabled, instead of telling the person
 * incorrect password, spring security by default would say the account is disabled.
 * This is a security issue imo. Why say the account is disabled when the password is not even correct?
 */
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
	private final AuthAccountService authAccountService;
	private final BCryptPasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		var email = authentication.getName();
		var password = authentication.getCredentials().toString();
		var authAccount = authAccountService.loadUserByUsername(email);
		var passwordIsValid = passwordEncoder.matches(password, authAccount.getPassword());
		var accountEnabled = authAccount.isEnabled();

		log.info("Authenticating user: {}", email);
		log.info("Password: {}", password);
		log.info("Password from DB: {}", authAccount.getPassword());

		// order of if statement is important. Only checking for disabled because everything else would be true
		if (passwordIsValid && !accountEnabled) { // correct password, but account is disabled
			throw new DisabledException("Account is disabled");
		}
		if (!passwordIsValid) { // incorrect password
			throw new BadCredentialsException("Invalid username or password");
		}
		return new UsernamePasswordAuthenticationToken(email, password, authAccount.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}