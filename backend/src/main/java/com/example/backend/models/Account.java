package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.Instant;
import java.util.Objects;

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
@Setter
@ToString
@RequiredArgsConstructor
public class Account {
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long accountId;

	protected Account() {
	}

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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		Account account = (Account) o;
		return accountId != null && Objects.equals(accountId, account.accountId);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}


}
