package com.example.backend.services;

import com.example.backend.models.Account;
import com.example.backend.repos.AccountRepo;
import com.example.backend.transfer_objects.RegisterData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

/**
 * Service class.
 * This class holds the important parts of doing anything related to a user's account
 *
 * @author Ife Sunmola
 */
@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountRepo accountRepo;
	private final BCryptPasswordEncoder passwordEncoder;

	/**
	 * Method to create a new account. This method assumes the data it receives is 100% valid
	 *
	 * @param registerData the registration data
	 * @return the newly created account
	 */
	public Account createAccount(RegisterData registerData) {
		Account account = new Account(
				registerData.getEmail().toLowerCase(Locale.ROOT),
				passwordEncoder.encode(registerData.getPassword()),
				true, //TODO: Email verification
				registerData.isAcceptedTerms(), // should always be true
				Instant.now()
		);
		return accountRepo.save(account);
	}



	public List<Account> getAllAccounts() {
		return accountRepo.findAll();
	}
}