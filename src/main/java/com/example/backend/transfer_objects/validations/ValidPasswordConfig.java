package com.example.backend.transfer_objects.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * ValidPassword Config class
 *
 * @author Ife Sunmola
 */
public class ValidPasswordConfig implements ConstraintValidator<ValidPassword, String> {
	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		return password != null && password.length() >= 7;
	}
}
