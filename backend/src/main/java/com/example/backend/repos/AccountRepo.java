package com.example.backend.repos;

import com.example.backend.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data access layer. This interface provides the methods we use to get information from our database
 *
 * @author Ife Sunmola
 */
@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
	Optional<Account> findByEmail(String email);
}
