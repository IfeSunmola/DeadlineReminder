package com.example.backend.repos;

import com.example.backend.models.Account;
import com.example.backend.models.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Ife Sunmola
 */
@Repository
public interface DeadlineRepo extends JpaRepository<Deadline, Long> {

	List<Deadline> findDeadlineByOwner(Account owner);
}
