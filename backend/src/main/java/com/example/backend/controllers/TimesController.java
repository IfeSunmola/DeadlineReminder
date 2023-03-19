package com.example.backend.controllers;

import com.example.backend.models.DueTime;
import com.example.backend.models.ReminderTime;
import com.example.backend.repos.DueTimeRepo;
import com.example.backend.repos.ReminderTimeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Handles request related to ReminderTime and DueTime.
 * <p>
 * Should really only fetch data since the user can't save new ReminderTime or DueTime
 *
 * @author Ife Sunmola
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/static")
public class TimesController {
	private final ReminderTimeRepo reminderTimeRepo;
	private final DueTimeRepo dueTimeRepo;

	@GetMapping("/reminder-times")
	public List<ReminderTime> getReminderTimes() {
		return reminderTimeRepo.findAll();
	}

	@GetMapping("/due-times")
	public List<DueTime> getDueTimes() {
		//return dueTimeRepo.findAll().stream().map(DueTime::getName).toList();
		return dueTimeRepo.findAll();
	}
}