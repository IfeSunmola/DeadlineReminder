package com.example.backend.services;

import com.example.backend.mailsender.EmailSenderService;
import com.example.backend.models.Account;
import com.example.backend.models.VerificationCode;
import com.example.backend.repos.AccountRepo;
import com.example.backend.security.AppProperties;
import com.example.backend.transfer_objects.LoginData;
import com.example.backend.transfer_objects.PasswordResetData;
import com.example.backend.transfer_objects.RegisterData;
import com.example.backend.transfer_objects.VerifyCodeData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.backend.mailsender.MailBody.PASSWORD_RESET_HTML;
import static com.example.backend.mailsender.MailBody.VERIFY_CODE_HTML;
import static com.example.backend.models.VerificationCodeType.EMAIL_VERIFICATION;
import static com.example.backend.models.VerificationCodeType.PASSWORD_RESET;

/**
 * Service class.
 * This class holds the important parts of doing anything related to a user's account
 *
 * @author Ife Sunmola
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
	private final AccountRepo accountRepo;
	private final BCryptPasswordEncoder passwordEncoder;
	private final VerificationCodeService codeService;
	private final EmailSenderService emailService;
	private final AppProperties appProps;
	private final JwtEncoder encoder;
	private final AuthenticationManager authManager;

	/**
	 * Method to validate the registration data gotten from the front end
	 *
	 * @param registerData the registration data
	 * @param result       contains all the errors gotten by the @Valid annotation
	 * @throws MethodArgumentNotValidException if there are any errors in the registration data. The exception is handled by ExceptionHandlerAdvice
	 */
	public void validateData(RegisterData registerData, BindingResult result) throws MethodArgumentNotValidException, NoSuchMethodException {
		var email = registerData.getEmail() == null ? "" : registerData.getEmail().toLowerCase(Locale.ROOT);
		var password = registerData.getPassword() == null ? "" : registerData.getPassword();
		var confirmPassword = registerData.getConfirmPassword() == null ? "" : registerData.getConfirmPassword();


		if (!password.equals(confirmPassword)) {
			result.rejectValue("confirmPassword", "confirmPassword.notMatch", "Passwords do not match");
		}
		else if (accountRepo.emailExists(email)) {
			result.rejectValue("email", "", "Email " + email + " is already in use");
		}

		if (result.hasFieldErrors()) {
			MethodParameter methodParameter = new MethodParameter(
					AccountService.class.getMethod("validateData", RegisterData.class, BindingResult.class),
					1
			);
			throw new MethodArgumentNotValidException(methodParameter, result);
		}
	}

	/**
	 * Method to create a new account. This method assumes the data it receives is 100% valid
	 *
	 * @param registerData the registration data
	 * @return the newly created account
	 */
	public Map<String, String> createAccount(RegisterData registerData) {
		Account savedAccount = accountRepo.save(
				new Account(
						registerData.getNickname(),
						registerData.getEmail().toLowerCase(Locale.ROOT),
						passwordEncoder.encode(registerData.getPassword()),
						false,
						registerData.isAcceptedTerms(), // should always be true
						Instant.now()
				)
		);
		System.out.println("Email:  " + savedAccount.getEmail());

		// returns a Map containing email and codeId
		return sendVerificationEmail(savedAccount);
	}

	@Transactional
	public Map<String, String> sendVerificationEmail(Account account) {
		// delete the verification code the user has before generating a new one
		codeService.deleteExistingCode(account);
		VerificationCode code = new VerificationCode(account, EMAIL_VERIFICATION);
		Long codeId = codeService.saveCode(code).getCodeId();
		String emailBody = VERIFY_CODE_HTML.replace("$firstName", account.getNickname())
				.replace("$verificationCode", code.getCode());

		emailService.sendMail(
				"Deadline Reminder: Activate your account",
				"donotreply@deadline-reminder.com",
				account.getEmail(),
				"donotreply@deadline-reminder.com",
				emailBody
		);

		log.info("Code sent: {}, created at: {} expires at {}", code.getCode(), code.getDateCreated().toString(), code.getExpiryDate().toString());
		return Map.of("email", account.getEmail(),
				"codeId", codeId.toString());
	}

	public boolean emailExists(String email) {
		return accountRepo.emailExists(email);
	}

	public void deleteAllAccounts() {
		accountRepo.deleteAll();
	}

	public Account findByEmail(String email) {
		return accountRepo.findByEmail(email).orElse(null);
	}

	public String verifyAccount(VerifyCodeData data) {
		VerificationCode code = codeService.findCodeById(data.codeId());
		Account account = findByEmail(data.userEmail());
		// code from db is not null, and the owner of the code is the account that was found by email
		// and the code is not expired and code from user is equal to code in db
		if (code == null || account == null) {
			return "Invalid Request"; // redirect to home
		}
		else if (code.getOwner().equals(account) && !code.isExpired() && code.getCode().equals(data.codeFromUser())) {
			account.setEnabled(true);
			accountRepo.save(account);
			codeService.deleteCode(code);
			return "Success";
		}
		else if (!code.getCode().equals(data.codeFromUser())) {
			return "Incorrect";
		}
		else if (code.isExpired()) {
			codeService.deleteCode(code);
			return "Expired";
		}
		else {
			log.info("Unhandled request: Verify Code data: {}, Code: {}, Account: {}", data, code, account);
			return "Unhandled Request"; // redirect to home
		}
	}

	public Map<String, String> authenticateUser(LoginData loginData) {
		String email = loginData.getEmail().toLowerCase(Locale.ROOT);
		String password = loginData.getPassword();
		boolean stayLoggedIn = loginData.getStayLoggedIn();

		Map<String, String> result = new HashMap<>();
		result.put("email", email);
		try {
			Authentication auth = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							email,
							password
					)
			);
			SecurityContextHolder.getContext().setAuthentication(auth);
			String token = generateToken(auth, stayLoggedIn);
			result.put("token", token);
		}
		catch (BadCredentialsException ex) {
			result.put("error", "Invalid username or password");
		}

		catch (DisabledException ex) {
			result.put("error", "Account is disabled");
		}
		catch (LockedException ex) {
			result.put("error", "Account is locked");
		}

		return result;
	}

	private String generateToken(Authentication auth, Boolean stayLoggedIn) {
		/*
		 * By default, the tokens will expire 60 seconds past their actual expiration time. This happens to account for clock drift.
		 * See the bean for JwtDecoder for how to remove the delay. HS256 Algorithm also used as default
		 **/
		Instant now = Instant.now();
		String scope = auth.getAuthorities().toString(); // role
		String email = auth.getName();
		int expirationTime = stayLoggedIn ? appProps.getJwtExpTime() : appProps.getJwtExpTimeDefault();
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("My Issuer ID")
				.issuedAt(now)
				.expiresAt(now.plus(expirationTime, ChronoUnit.HOURS))
				.subject(email)
				.claim("scope", scope)
				.build();
		return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}

	@Transactional
	public void sendResetPasswordEmail(String email) {
		// here, we don't want to give the user any information about whether the email exists or not
		// so we just return a success message
		if (!accountRepo.emailExists(email)) {
			return;
		}
		var account = findByEmail(email);
		// delete the verification code the user has before generating a new one
		codeService.deleteExistingCode(account);
		VerificationCode code = new VerificationCode(account, PASSWORD_RESET);
		String uuid = codeService.saveCode(code).getCode();

		String resetLink = String.format("%s/reset-password/confirm-reset?token=%s", appProps.getFrontendSite(), uuid);
		String emailBody = PASSWORD_RESET_HTML.replace("$nickname", account.getNickname())
				.replace("$resetLink", resetLink);

		emailService.sendMail(
				"Deadline Reminder: Reset your password",
				"donotreply@deadline-reminder.com",
				email,
				"donotreply@deadline-reminder.com",
				emailBody
		);

		log.info("Password reset sent. {}", resetLink);
	}

	private boolean codeIsValid(VerificationCode code) {
		return code != null && !code.isExpired();
	}

	public String verifyPasswordResetToken(String token) {
		VerificationCode code = codeService.findCodeByCode(token);
		if (codeIsValid(code)) {
			return code.getOwner().getEmail();
		}
		return "Expired";
	}

	public Map<String, String> confirmPasswordReset(PasswordResetData passwordResetData) {
		VerificationCode code = codeService.findCodeByCode(passwordResetData.getToken());
		if (codeIsValid(code)) {
			Account account = code.getOwner();
			account.setPassword(passwordEncoder.encode(passwordResetData.getPassword()));
			accountRepo.save(account);
			codeService.deleteCode(code);
			return Map.of("message", "Success");
		}
		return Map.of("token", "Expired");
	}

	public void validatePasswordResetData(PasswordResetData resetData, BindingResult result)
			throws MethodArgumentNotValidException, NoSuchMethodException {
		var email = resetData.getEmail() == null ? "" : resetData.getEmail().toLowerCase(Locale.ROOT);
		var token = resetData.getToken();
		var password = resetData.getPassword();
		var confirmPassword = resetData.getConfirmPassword();
		VerificationCode code = codeService.findCodeByCode(token);
		var emailFromCode = code == null ? "" : code.getOwner().getEmail();

		if (code == null || code.isExpired()) { // code is expired/invalid
			result.rejectValue("token", "token.expired", "Invalid token");
		}
		// the email linked to the code is not the same as the email in the request || email from request does not exist
		else if (!email.equals(emailFromCode) || !accountRepo.emailExists(email)) {
			codeService.deleteExistingCode(code.getOwner());
			result.rejectValue("email", "email.invalid", "Invalid email");
		}
		else if (password == null || password.isBlank()) { // password is empty
			codeService.deleteExistingCode(code.getOwner());
			codeService.deleteCode(code);
			result.rejectValue("password", "password.blank", "Password cannot be blank");
		}
		else if (confirmPassword == null || confirmPassword.isBlank()) { // confirm password is empty
			codeService.deleteExistingCode(code.getOwner());
			result.rejectValue("confirmPassword", "confirmPassword.blank", "Confirm password cannot be blank");
		}
		else if (!password.equals(confirmPassword)) { // passwords don't match
			codeService.deleteExistingCode(code.getOwner());
			result.rejectValue("confirmPassword", "confirmPassword.mismatch", "Passwords do not match");
		}

		if (result.hasFieldErrors()) {
			MethodParameter methodParameter = new MethodParameter(
					this.getClass().getMethod("validatePasswordResetData", PasswordResetData.class, BindingResult.class),
					1
			);
			throw new MethodArgumentNotValidException(methodParameter, result);
		}
	}
}