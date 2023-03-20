package com.example.backend.repos.constants;

import com.example.backend.models.constants.ReminderTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Not creating a service class for this because we're not doing much with it
 *
 * @author Ife Sunmola
 */
@Repository
public interface ReminderTimeRepo extends JpaRepository<ReminderTime, Long> {

}
