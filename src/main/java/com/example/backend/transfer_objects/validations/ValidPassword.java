package com.example.backend.transfer_objects.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for password length validation
 *
 * @author Ife Sunmola
 */
@Constraint(validatedBy = {ValidPasswordConfig.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
	String message() default "Password must be at least 7 characters long";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
