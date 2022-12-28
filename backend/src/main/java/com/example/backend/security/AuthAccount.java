package com.example.backend.security;

import com.example.backend.models.Account;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * This class represents an account that has been logged in or that is about to be logged in.
 * <p>
 * To create an instance of this class, we need to pass the account object
 *
 * @author Ife Sunmola
 */
@AllArgsConstructor
public class AuthAccount implements UserDetails {
	private final Account account;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("USER"));
	}

	@Override
	public String getPassword() {
		return account.getPassword();
	}

	@Override
	public String getUsername() {
		return account.getEmail();
	}

	@Override
	public boolean isEnabled() {
		return account.isEnabled();
	}

	// Not needed for this project but spring security requires them
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
}