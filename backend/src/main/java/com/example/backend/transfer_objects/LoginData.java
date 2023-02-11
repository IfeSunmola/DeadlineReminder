package com.example.backend.transfer_objects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * POJO to hold a user's login information from the front end
 *
 * @author Ife Sunmola
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginData {
	@Email(message = "A valid email is required")
	private String email;

	private String password;

	@NotNull(message = "True or False is required")
	private Boolean stayLoggedIn;
}
