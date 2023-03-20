package com.example.backend.repos;

import com.example.backend.models.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ife Sunmola
 */
@Repository
public interface ReminderRepo extends JpaRepository<Reminder, Long> {
}
