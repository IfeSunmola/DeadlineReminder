package com.example.backend.repos;

import com.example.backend.models.Account;
import com.example.backend.models.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Ife Sunmola
 */
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
	boolean existsByOwner(Account owner);

	void deleteByOwner(Account owner);
}