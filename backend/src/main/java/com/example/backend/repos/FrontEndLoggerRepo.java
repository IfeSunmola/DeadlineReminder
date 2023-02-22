package com.example.backend.repos;

import com.example.backend.models.LogBody;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Data access layer. This interface provides the methods we use to get information from our database
 *
 * @author Ife Sunmola
 */
@Repository
public interface FrontEndLoggerRepo extends JpaRepository<LogBody, Long> {
}
