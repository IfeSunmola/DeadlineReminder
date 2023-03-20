package com.example.backend.services;

import com.example.backend.models.Deadline;
import com.example.backend.models.Reminder;
import com.example.backend.models.constants.ReminderTime;
import com.example.backend.repos.ReminderRepo;
import com.example.backend.repos.constants.ReminderTimeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Ife Sunmola
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderService {
	private final ReminderTimeRepo reminderTimeRepo;
	private final ReminderRepo reminderRepo;

	public void saveReminders(Deadline deadline, ArrayList<String> timesToRemind) {
		var all = reminderTimeRepo.findAll();
		ArrayList<ReminderTime> validTimes = all.stream()
				.filter(timeInDb -> timesToRemind.contains(timeInDb.getName()))
				.collect(Collectors.toCollection(ArrayList::new));

		if (validTimes.size() != timesToRemind.size()) {
			// handle exception here
			log.warn("Some reminder times are invalid");
			return;
		}

		Instant dueDateTime = deadline.getDueDateTime();

		validTimes.forEach(time ->
				reminderRepo.save(
						new Reminder(
								dueDateTime.minus(time.getTimeInMinutes(), ChronoUnit.MINUTES),
								deadline)
				)
		);
	}
}