package com.example.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

/**
 * This class handles exceptions related to authentications. When an authentication exception is
 * thrown from anywhere in the application, it will be handled here
 * <p>
 * Since we aren't using the isLocked feature of spring security, LockedException will never be thrown. However,
 * it is included here for completeness.
 *
 * @author Ife Sunmola
 */
@RestControllerAdvice
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationExceptionHandler {
	@ExceptionHandler(BadCredentialsException.class)
	public Map<String, String> badCredentials() {
		return Collections.singletonMap("error", "Invalid username or password");
	}

	@ExceptionHandler(LockedException.class)
	public Map<String, String> lockedAccount() {
		return Collections.singletonMap("error", "Account is locked");
	}

	@ExceptionHandler(DisabledException.class)
	public Map<String, String> disabledAccount() {
		return Collections.singletonMap("error", "Account is disabled");
	}
}