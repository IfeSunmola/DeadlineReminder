package com.example.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * @author Ife Sunmola
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@RequiredArgsConstructor
public class Reminder {
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long reminderId;

	@NonNull
	private Instant timeToRemind;

	@NonNull
	@ManyToOne @JoinColumn(name = "deadline_id", referencedColumnName = "deadline_id")
	private Deadline deadline;
}