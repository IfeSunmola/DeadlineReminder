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
 * In frontend, the times that shows in the drop-down. E.g 7:00 AM, 7:30 AM, 8:00 AM, etc
 * <p>
 * In this class, and Reminder time, I need to use an ID, so it can maintain the order of insertion.
 * <p>
 * If I use the name as the ID, 10:00AM will show first in the drop-down, but I want 7:00AM to show first.
 * <p>
 *
 * @author Ife Sunmola
 */
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
public class DueTime {
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long dueTimeId;

	@NonNull private String name;
}