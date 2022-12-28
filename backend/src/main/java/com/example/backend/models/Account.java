package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/**
 * This class represents the Account table in the database.
 * ORM, provided by JPA, maps the Account class to the Account table.
 * <br>
 * `@NonNull` is from Lombok so @RequiredArgsConstructor can work<br>
 * We don't need to make the setters public because they should not be modified after creation.
 * Jpa can use private setters and protected NoArgsConstructor
 *
 * @author Ife Sunmola
 */
@Entity
@Table(name = "accounts")
@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Account {
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long accountId;

	@Column(unique = true) @NonNull
	private String email;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // don't ever send password to client
	@NonNull
	private String password;

	@NonNull
	private boolean isEnabled;

	@NonNull
	private boolean acceptedTerms;

	@NonNull @Temporal(TemporalType.TIMESTAMP)
	private Instant dateCreated;
}
