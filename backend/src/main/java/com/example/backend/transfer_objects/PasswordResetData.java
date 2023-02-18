package com.example.backend.transfer_objects;

import com.example.backend.transfer_objects.validations.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * @author Ife Sunmola
 */
@Getter
public class PasswordResetData {
	private String token;
	@Email(message = "A valid email is required")
	@NotBlank(message = "Email is required")
	private String email;

	@ValidPassword
	private String password;
	@ValidPassword
	private String confirmPassword;
}
