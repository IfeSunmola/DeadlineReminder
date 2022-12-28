package com.example.backend.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * This exception will be thrown when the email is not found in the database.
 * <br>
 * Normally, we throw UsernameNotFoundException butm this makes it clear because we use email, not username
 *
 * @author Ife Sunmola
 */
public class EmailNotFoundException extends UsernameNotFoundException {
	public EmailNotFoundException(String email) {
		super("Email '" + email + "' was not found");
	}
}
