package com.example.backend.services;

import com.example.backend.mailsender.EmailSenderService;
import com.example.backend.models.Account;
import com.example.backend.models.VerificationCode;
import com.example.backend.repos.AccountRepo;
import com.example.backend.security.AppProperties;
import com.example.backend.transfer_objects.RegisterData;
import com.example.backend.transfer_objects.VerifyCodeData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Map;

import static com.example.backend.mailsender.MailBody.VERIFY_CODE_HTML;

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
		//TODO: Email verification
		Account savedAccount = accountRepo.save(
				new Account(
						registerData.getNickname(),
						registerData.getEmail().toLowerCase(Locale.ROOT),
						passwordEncoder.encode(registerData.getPassword()),
						true,
						registerData.isAcceptedTerms(), // should always be true
						Instant.now()
				)
		);
		System.out.println("Email:  " + savedAccount.getEmail());
		// delete the verification code the user has before generating a new one
		if (codeService.accountHasCode(savedAccount)) {
			codeService.deleteCodeByAccount(savedAccount);
		}
		// generate and send new verification code
		VerificationCode code = new VerificationCode(savedAccount);
		Long codeId = codeService.saveCode(code).getCodeId();
		String emailBody = VERIFY_CODE_HTML.replace("$firstName", savedAccount.getEmail())
				.replace("$verificationCode", code.getCode());

		emailService.sendMail(
				"Deadline Reminder: Activate your account",
				"donotreply@deadline-reminder.com",
				savedAccount.getEmail(),
				"donotreply@deadline-reminder.com",
				emailBody
		);

		log.info("Code sent: {}, created at: {} expires at {}", code.getCode(), code.getDateCreated().toString(), code.getExpiryDate().toString());
		return Map.of("email", savedAccount.getEmail(),
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

	// jwt
	public String authenticateUser(String email, String password, Boolean stayLoggedIn) {
		Authentication auth = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						email,
						password
				)
		);
		SecurityContextHolder.getContext().setAuthentication(auth);

		return generateToken(auth, stayLoggedIn);
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
}