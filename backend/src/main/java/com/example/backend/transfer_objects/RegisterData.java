package com.example.backend.transfer_objects;

import com.example.backend.transfer_objects.validations.ValidPassword;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * POJO to hold the new account information before storing in the database
 *
 * @author Ife Sunmola
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterData {
	@NotBlank(message = "Nickname is required")
	private String nickname;

	@Email(message = "A valid email is required")
	@NotBlank(message = "Email is required")
	private String email;

	@ValidPassword // custom annotation, using default message in ValidPassword.java
	private String password;

	@ValidPassword
	private String confirmPassword;

	@AssertTrue(message = "You must agree to the terms and conditions")
	private boolean acceptedTerms;
}
