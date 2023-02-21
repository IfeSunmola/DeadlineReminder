package com.example.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class handles exceptions related to authentications. When an authentication exception is
 * thrown from anywhere in the application, it will be handled here
 * <p>
 * Since we aren't using the isLocked feature of spring security, LockedException will never be thrown. However,
 * it is included here for completeness.
 * <p>
 * All exceptions are handled by returning a 401 status code and a message
 *
 * @author Ife Sunmola
 */
@RestControllerAdvice
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationExceptionHandler {
	@ExceptionHandler(BadCredentialsException.class)
	public String badCredentials(BadCredentialsException ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(DisabledException.class)
	public String disabledAccount(DisabledException ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public String emailNotFound(UsernameNotFoundException ex) {
		return ex.getMessage();
	}
}