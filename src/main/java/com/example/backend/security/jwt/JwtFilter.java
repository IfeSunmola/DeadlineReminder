package com.example.backend.security.jwt;

import com.example.backend.exceptions.BadRequestException;
import com.example.backend.security.AuthAccount;
import com.example.backend.security.AuthAccountService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Package protected class that intercepts requests and checks if the request has a valid JWT token. If the token is valid,
 * the user is authenticated.
 *
 * @author Ife Sunmola
 * @see <a href="https://www.baeldung.com/spring-onceperrequestfilter">Explanation</a>
 */
@Component
@RequiredArgsConstructor
class JwtFilter extends OncePerRequestFilter {
	private final JwtUtils jwtUtils;
	private final AuthAccountService authAccountService;


	/**
	 * Method to ...  imma be honest I don't really understand how this works 😭😭😭
	 * I know what's happening, I just can't explain it
	 *
	 * @param request     HttpServletRequest
	 * @param response    HttpServletResponse
	 * @param filterChain FilterChain
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
		// Gets the Bearer token in format of Bearer eyJH-blah-blah-blah-very-very-long-string-the-dashes-are-just-for-visuals-not-part-of-the-token
		try {
			String authHeader = request.getHeader(AUTHORIZATION);

			String token = null; // should probably use optionals for this
			String email = null;

			// if the authHeader is not empty and starts with "Bearer ", get the token, and extract the email from it
			if (!ObjectUtils.isEmpty(authHeader) && authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7);
				email = jwtUtils.extractEmail(token);
			}

			// Authentication
			SecurityContext securityContext = SecurityContextHolder.getContext();
			if (email != null && securityContext.getAuthentication() == null) {
				AuthAccount account = authAccountService.loadUserByUsername(email);

				if (jwtUtils.validateToken(token, account)) { // Valid token, log the user in
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							account,
							null,
							account.getAuthorities()
					);
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					securityContext.setAuthentication(authToken); // authenticate the user
				}
			}
			filterChain.doFilter(request, response); // This throws IOException and ServletException and does some magic I don't understand
		}
		catch (IOException | ServletException e) {
			throw new BadRequestException("An error occurred while trying to filter the request ... " + e.getMessage());
		}

	}
}