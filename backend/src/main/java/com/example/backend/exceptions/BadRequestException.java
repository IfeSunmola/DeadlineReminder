package com.example.backend.exceptions;

/**
 * This exception will be thrown when a request from the client is invalid
 *
 * @author Ife Sunmola
 */
public class BadRequestException extends RuntimeException {
	public BadRequestException(String message) {
		super(message);
	}
}
