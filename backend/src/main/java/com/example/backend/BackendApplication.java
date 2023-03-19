package com.example.backend;

import com.example.backend.models.DueTime;
import com.example.backend.models.ReminderTime;
import com.example.backend.repos.DueTimeRepo;
import com.example.backend.repos.ReminderTimeRepo;
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
	private final ReminderTimeRepo reminderTimeRepo;
	private final DueTimeRepo dueTimeRepo;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String... args) {
		if (accountService.getAllAccounts().size() == 0) {
			codeService.deleteAllCodes();
			RegisterData data = new RegisterData("Ife", "ifesunmola001@gmail.com", "password", "password", true);
			authService.createAccount(data);

			RegisterData data1 = new RegisterData("Cassi", "cassi@heartbreaker.com", "password", "password", true);
			authService.createAccount(data1);

			RegisterData data2 = new RegisterData("Katara", "katara@waterbender.com", "password", "password", true);
			authService.createAccount(data2);
		}

		// add the reminder times
		if (reminderTimeRepo.findAll().isEmpty()) {
			reminderTimeRepo.save(new ReminderTime("30 minutes", false));
			reminderTimeRepo.save(new ReminderTime("1 hour", true));
			reminderTimeRepo.save(new ReminderTime("2 hours", false));
			reminderTimeRepo.save(new ReminderTime("3 hours", false));
			reminderTimeRepo.save(new ReminderTime("4 hours", true));
		}

		// add the due times
		if (dueTimeRepo.findAll().isEmpty()) {
			for (int i = 7; i < 12; i++) { // save the AM times
				dueTimeRepo.save(new DueTime(i + ":00 AM"));
				dueTimeRepo.save(new DueTime(i + ":30 AM"));
			}
			dueTimeRepo.save(new DueTime("12:00 PM (Noon)"));
			dueTimeRepo.save(new DueTime("12:30 PM"));

			for (int i = 1; i < 12; i++) { // save the PM times
				dueTimeRepo.save(new DueTime(i + ":00 PM"));
				dueTimeRepo.save(new DueTime(i + ":30 PM"));
			}
			dueTimeRepo.save(new DueTime("12:00 AM (Midnight)"));
		}
	}
}
