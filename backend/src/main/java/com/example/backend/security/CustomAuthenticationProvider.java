package com.example.backend.security;

import com.nimbusds.jose.shaded.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static com.example.backend.AppConstants.NO_PASSWORD_NEEDED;

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
		// sent back to the client if authentication is not successful
		Map<String, String> error = new HashMap<>();
		error.put("email", email);
		Gson gson = new Gson();

		log.info("Authenticating user/password: {}, {}", email, password);

		// this really isn't ideal, but I need a way to authenticate the user immediately after verifying their account
		if (password.equals(NO_PASSWORD_NEEDED) && accountEnabled) {
			log.info("No password needed to authenticated this user");
			return new UsernamePasswordAuthenticationToken(email, NO_PASSWORD_NEEDED, authAccount.getAuthorities());
		}

		// order of if statement is important. Only checking for disabled because everything else would be true
		if (passwordIsValid && !accountEnabled) { // correct password, but account is disabled
			error.put("error", "Account is disabled");
			throw new DisabledException(gson.toJson(error));
		}
		if (!passwordIsValid) { // incorrect password
			error.put("error", "Invalid username or password");
			throw new BadCredentialsException(gson.toJson(error));
		}
		return new UsernamePasswordAuthenticationToken(email, password, authAccount.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}