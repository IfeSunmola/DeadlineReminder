package com.example.backend;

import com.example.backend.services.AccountService;
import com.example.backend.transfer_objects.RegisterData;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class BackendApplication implements CommandLineRunner {
	private final AccountService accountService;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if (accountService.getAllAccounts().size() > 0) {
			return;
		}

		RegisterData data = new RegisterData("sunmolaife@gmail.com", "password", "password", true);
		accountService.createAccount(data);

		RegisterData data1 = new RegisterData("they@them.com", "password", "password", true);
		accountService.createAccount(data1);

		RegisterData data2 = new RegisterData("hehehe@boy.com", "password", "password", true);
		accountService.createAccount(data2);

	}
}
