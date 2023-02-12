package com.example.backend;

import com.example.backend.security.AppProperties;
import com.example.backend.services.AccountService;
import com.example.backend.services.VerificationCodeService;
import com.example.backend.transfer_objects.RegisterData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@AllArgsConstructor
@EnableConfigurationProperties(AppProperties.class)
public class BackendApplication implements CommandLineRunner {
	private final AccountService accountService;
	private final VerificationCodeService codeService;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String... args) {
		codeService.deleteAllCodes();
		accountService.deleteAllAccounts();

		RegisterData data = new RegisterData("sunmolaife@gmail.com", "password", "password", true);
		accountService.createAccount(data);

		RegisterData data1 = new RegisterData("they@them.com", "password", "password", true);
		accountService.createAccount(data1);

		RegisterData data2 = new RegisterData("hehehe@boy.com", "password", "password", true);
		accountService.createAccount(data2);
	}
}
