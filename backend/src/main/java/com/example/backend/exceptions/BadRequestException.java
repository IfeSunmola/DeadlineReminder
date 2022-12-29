package com.example.backend.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * This exception will be thrown when a request from the client is invalid
 *
 * @author Ife Sunmola
 */
@Slf4j
public class BadRequestException extends RuntimeException {
	public BadRequestException(String message) {
		super(message);
		log.error(message);
	}
}
