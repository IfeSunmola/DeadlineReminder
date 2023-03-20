package com.example.backend.models.constants;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * In frontend, the "30 minutes", "1 hour", "2 hours", etc
 * <p>
 * See DueTime docs for more info
 *
 * @author Ife Sunmola
 */
@Entity
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class ReminderTime {
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long reminderTimeId;

	@NonNull private String name;

	@NonNull private boolean checkedByDefault;

	@NonNull private long timeInMinutes;
}
