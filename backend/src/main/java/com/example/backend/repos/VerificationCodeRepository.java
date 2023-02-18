package com.example.backend.repos;

import com.example.backend.models.Account;
import com.example.backend.models.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author Ife Sunmola
 */
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
	boolean existsByOwner(Account owner);

	void deleteByOwner(Account owner);

	@Query("select v from VerificationCode v where v.code = ?1")
	Optional<VerificationCode> findByCode(String code);

}