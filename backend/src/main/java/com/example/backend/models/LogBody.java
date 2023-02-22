package com.example.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
	private Instant timeStamp;
	private String logLevel;
	private String fileName;
	private String message;
	@Column(length = 20000)
	private String attachment;
}
