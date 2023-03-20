package com.example.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Date;

/**
 * @author Ife Sunmola
 */
@Entity
@Getter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class Deadline {
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "deadline_id")
	private Long deadlineId;

	@NonNull private String title;
	@NonNull private Instant dueDateTime;
	@NonNull private boolean otherPeopleSee;

	@ManyToOne
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	@NonNull private Account owner;

	private boolean isCompleted;

	public boolean isPastDue() {
		return dueDateTime.isBefore(Instant.now());
	}

	public Date extractDate() {
		return Date.from(dueDateTime);
	}

	public String extractTime() {
		return dueDateTime.toString().substring(11, 16);
	}
}