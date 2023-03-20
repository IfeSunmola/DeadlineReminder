package com.example.backend.repos;

import com.example.backend.models.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ife Sunmola
 */
@Repository
public interface DeadlineRepo extends JpaRepository<Deadline, Long> {
}
