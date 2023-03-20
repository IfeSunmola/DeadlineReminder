package com.example.backend.controllers;

import com.example.backend.services.DeadlineService;
import com.example.backend.transfer_objects.NewDeadlineData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ife Sunmola
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/deadlines")
@Slf4j
public class DeadlineController {
	private final DeadlineService deadlineService;

	@PostMapping("/save")
	@PreAuthorize("isAuthenticated()")
	public void saveDeadline(@RequestBody NewDeadlineData newDeadlineData) {
		// if email passed is not equal to current context, error
		log.info("Deadline gotten: {}", newDeadlineData);
		deadlineService.saveDeadline(newDeadlineData);
	}
}