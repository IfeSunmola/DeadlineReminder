package com.example.backend.security;

import com.example.backend.repos.AccountRepo;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service class for the AuthAccount class.
 * <p>
 * This class is mainly used by spring security to get the currently authenticated account
 *
 * @author Ife Sunmola
 */
@AllArgsConstructor
@Service
public class AuthAccountService implements UserDetailsService {
	private final AccountRepo accountRepo;

	/**
	 * Method to find a user by their username. In our case, by their email
	 *
	 * @param email the email to load the user by
	 * @return the AuthAccount if the email associated to it was found
	 * @throws UsernameNotFoundException if the email was not found in the database
	 * @author Ife Sunmola
	 */
	@Override
	public AuthAccount loadUserByUsername(String email) throws UsernameNotFoundException {
		Map<String, String> error = new HashMap<>();
		error.put("email", email);
		error.put("error", "Invalid username or password");
		Gson gson = new Gson();
		return new AuthAccount(accountRepo.findByEmail(email)
				.orElseThrow(
						() -> new UsernameNotFoundException(gson.toJson(error))
				)
		);
	}
}