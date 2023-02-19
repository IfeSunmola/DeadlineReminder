package com.example.backend.services;

import com.example.backend.models.Account;
import com.example.backend.models.VerificationCode;
import com.example.backend.repos.VerificationCodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public void deleteExistingCode(Account owner) {
		if (accountHasCode(owner)) {
			codeRepo.deleteByOwner(owner);
		}
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

	public VerificationCode findCodeByCode(String code) {
		return codeRepo.findByCode(code).orElse(null);
	}
}