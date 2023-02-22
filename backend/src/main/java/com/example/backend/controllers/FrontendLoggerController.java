package com.example.backend.controllers;

import com.example.backend.models.LogBody;
import com.example.backend.services.FrontendLoggerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ife Sunmola
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/frontend")
@Slf4j
public class FrontendLoggerController {
	private final FrontendLoggerService loggerService;

	@PostMapping("/log")
	public void log(@RequestBody LogBody logBody) {
		loggerService.saveToLog(logBody);
	}
}