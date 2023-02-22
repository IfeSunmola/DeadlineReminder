package com.example.backend;

import com.example.backend.security.AppProperties;
import com.example.backend.services.AccountService;
import com.example.backend.services.AuthService;
import com.example.backend.services.VerificationCodeService;
import com.example.backend.transfer_objects.RegisterData;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@AllArgsConstructor
@EnableConfigurationProperties(AppProperties.class)
public class BackendApplication implements CommandLineRunner {
	private final AuthService authService;
	private final AccountService accountService;
	private final VerificationCodeService codeService;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String... args) {
		if (accountService.getAllAccounts().size() == 0) {
			codeService.deleteAllCodes();
			RegisterData data = new RegisterData("Ife", "ifesunmola001@gmail.com", "password", "password", true);
			authService.createAccount(data);

			RegisterData data1 = new RegisterData("Cassi", "cassi@hearbreaker.com", "password", "password", true);
			authService.createAccount(data1);

			RegisterData data2 = new RegisterData("Katara", "katara@waterbender.com", "password", "password", true);
			authService.createAccount(data2);
		}

	}
}
