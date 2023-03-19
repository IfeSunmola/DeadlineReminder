package com.example.backend.repos;

import com.example.backend.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data access layer. This interface provides the methods we use to get information from our database
 *
 * @author Ife Sunmola
 */
@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
	@Query("select a from Account a where upper(a.email) = upper(?1)")
	Optional<Account> findByEmail(String email);

	@Query("select (count(a) > 0) from Account a where upper(a.email) = upper(?1)")
	boolean emailExists(String email);
}
