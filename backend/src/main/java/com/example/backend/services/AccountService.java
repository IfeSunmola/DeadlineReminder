package com.example.backend.services;

import com.example.backend.models.Account;
import com.example.backend.repos.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class.
 * This class holds the important parts of doing anything related to a user's account
 *
 * @author Ife Sunmola
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
	private final AccountRepo accountRepo;

	public List<Account> getAllAccounts() {
		return accountRepo.findAll();
	}

	public Account findByEmail(String email) {
		return accountRepo.findByEmail(email).orElse(null);
	}
}