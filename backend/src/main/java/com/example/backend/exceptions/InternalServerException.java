package com.example.backend.exceptions;

import org.slf4j.Logger;

/**
 * This exception will be thrown when a something went wrong on the server<br<br>. Typically, we would through this
 * for anything that goes wrong due to ... well, internal server problems
 * <p>
 * E.g. an exception wasn't handled properly, a required info in application.properties is missing, etc.
 *
 * @author Ife Sunmola
 */
public class InternalServerException extends RuntimeException {
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(InternalServerException.class);

	public InternalServerException(String message) {
		super(message);
		log.error(message);
	}
}
