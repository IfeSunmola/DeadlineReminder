package com.example.backend.security.jwt;

import com.example.backend.exceptions.InternalServerException;
import com.example.backend.security.AuthAccount;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * This class holds the logic for generating a token, validating it, etc
 *
 * @author Ife Sunmola
 */
@Service
@RequiredArgsConstructor
public class JwtUtils {
	private final String KEY = "THIS_IS_A_SECRET"; //TODO: Generate more secret key before prod
	private final Environment env;

	/**
	 * Method to extract the email from a jwt token. Recall that the email was added as the subject (also a claim)
	 *
	 * @param token the token to extract from
	 * @return String - the extracted email
	 */
	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * Method to extract the expiry date from a jwt token
	 *
	 * @param token the token to extract from
	 * @return Date: the extracted date
	 */
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	/**
	 * Method to extract all the claims from a jwt token
	 * <p>
	 * When a jwt token is decrypted the claims are usually in key value format.
	 *
	 * @param token          the token to decrypt from
	 * @param claimsResolver the function to apply to the claims on
	 * @return Generic T containing the extracted claims
	 * @see #createToken
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Method to validate the token by comparing the email linked to it to the email passed
	 * as an argument
	 *
	 * @param token       the token containing an email
	 * @param authAccount the account to check against
	 * @return true if the email in the token matches the email in the account, and false if not
	 */
	public Boolean validateToken(String token, AuthAccount authAccount) {
		String email = extractEmail(token);// email == username
		return (email.equals(authAccount.getUsername()) && !isTokenExpired(token));
	}

	/**
	 * Public driver method to generate a token from an AuthAccount
	 *
	 * @param account the account to generate the token from
	 * @return String: the generated token
	 */
	public String generateToken(AuthAccount account) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, account);
	}

	/**
	 * Private helper method to extract all the claims from a token
	 * <p>
	 * Recall: Claims hold information that can be used in the front end for authentication/verification
	 *
	 * @param token the token to extract from
	 * @return Claims: the extracted claims
	 */
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
	}

	/**
	 * Private helper method to check if a token has expired
	 *
	 * @param token the token to check
	 * @return true if the token has expired, and false if not
	 */
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}


	/**
	 * Private helper method to create a token from an AuthAccount
	 *
	 * @param claims  the claims to add to the token
	 * @param account the account to generate the token from
	 * @return String: the generated token
	 */
	private String createToken(Map<String, Object> claims, AuthAccount account) {
		long time = getExpirationTime(); // How long the token is valid for

		return Jwts.builder()
				.setClaims(claims)
				.claim("Authorities", account.getAuthorities())
				.setSubject(account.getUsername()) // username = email
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(time)))
				.signWith(SignatureAlgorithm.HS512, KEY) // sign the key
				.compact(); // make it url safe
	}

	/**
	 * Private helper method to get the expiration time from the environment variables. i.e. application.properties
	 *
	 * @return long: the expiration time
	 */
	private long getExpirationTime() {
		// If the gotten value is null, throw InternalServerException. I can't understand it if I don't space it out like this so
		// sorry to whoever is gonna read this lmao
		return Long.parseLong(
				Optional.ofNullable(env.getProperty("jwt.expiration.time"))
						.orElseThrow(
								() -> new InternalServerException("Expiration time was not set in applications.properties. " +
										"Format is: jwt.expiration.time=TIME")
						)
		);
	}
}