package com.example.backend.services;

import com.example.backend.models.Account;
import com.example.backend.models.VerificationCode;
import com.example.backend.repos.VerificationCodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Ife Sunmola
 */
@Service
@AllArgsConstructor
public class VerificationCodeService {
	private final VerificationCodeRepository codeRepo;

	public VerificationCode saveCode(VerificationCode code) {
		return codeRepo.save(code);
	}

	public boolean accountHasCode(Account owner) {
		return codeRepo.existsByOwner(owner);
	}

	public void deleteCodeByAccount(Account owner) {
		codeRepo.deleteByOwner(owner);
	}

	public void deleteAllCodes() {
		codeRepo.deleteAll();
	}

	public VerificationCode findCodeById(Long codeId) {
		return codeRepo.findById(codeId).orElse(null);
	}

	public void deleteCode(VerificationCode code) {
		codeRepo.delete(code);
	}
}