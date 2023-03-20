package com.example.backend.services;

import com.example.backend.models.Account;
import com.example.backend.models.Deadline;
import com.example.backend.repos.DeadlineRepo;
import com.example.backend.transfer_objects.NewDeadlineData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.ZoneOffset.UTC;

/**
 * @author Ife Sunmola
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeadlineService {
	private final DeadlineRepo deadlineRepo;
	private final AccountService accountService;
	private final ReminderService reminderService;

	public void saveDeadline(NewDeadlineData newDeadlineData) {
		Account owner = accountService.findByEmail(newDeadlineData.email());
		if (owner == null) {// shouldn't happen since user needs to be logged in
			log.warn("User with email {} not found", newDeadlineData.email());
			// handle exception here
			return;
		}
		Deadline deadline = deadlineRepo.save(
				new Deadline(
						newDeadlineData.title(),
						getDueDateTime(newDeadlineData.dueDate(), newDeadlineData.dueTime()),
						newDeadlineData.otherPeopleSee(),
						owner
				));
		log.info("Deadline saved: {}", deadline);
		reminderService.saveReminders(deadline, newDeadlineData.timesToRemind());
	}

	private Instant getDueDateTime(Instant dueDate, String dueTime) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(dueDate, UTC);

		String[] time = dueTime.split(":");
		int hour = Integer.parseInt(time[0]);
		int minute = Integer.parseInt(time[1].substring(0, 2));
		String ampm = time[1].substring(2, 5);

		hour = ampm.contains("PM") ? hour + 12 : hour;

		return localDateTime.withHour(hour).withMinute(minute).toInstant(UTC);
	}

	public List<Deadline> findByEmail(String email) {
		email = email.toLowerCase();
		var emailFromContext = SecurityContextHolder.getContext().getAuthentication().getName();
		if (!email.equals(emailFromContext)) {
			log.warn("Email passed is not the same as email from context");
			return null;
		}
		Account owner = accountService.findByEmail(email);
		if (owner == null) { // shouldn't happen.
			return null;
		}
		return deadlineRepo.findDeadlineByOwner(owner);
	}
}