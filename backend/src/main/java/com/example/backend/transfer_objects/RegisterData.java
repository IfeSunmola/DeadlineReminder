package com.example.backend.transfer_objects;

import com.example.backend.transfer_objects.validations.ValidPassword;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * POJO to hold the new account information before storing in the database
 *
 * @author Ife Sunmola
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterData {
	@Email(message = "A valid email is required")
	private String email;

	@ValidPassword // custom annotation, using default message in ValidPassword.java
	private String password;

	@ValidPassword
	private String confirmPassword;

	@AssertTrue(message = "You must agree to the terms and conditions")
	private boolean acceptedTerms;
}
