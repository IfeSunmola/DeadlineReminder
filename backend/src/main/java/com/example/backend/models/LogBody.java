package com.example.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * @author Ife Sunmola
 */
@Entity
@NoArgsConstructor
@Table(name = "frontend_logs")
@Getter
@Setter
@ToString
public class LogBody {
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE) private Long logId;
	private Instant timestamp;
	private String logLevel;
	private String fileName;
	private long lineNumber;
	private String message;
}
