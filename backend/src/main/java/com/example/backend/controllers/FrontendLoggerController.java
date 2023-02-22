package com.example.backend.controllers;

import com.example.backend.models.LogBody;
import com.example.backend.services.FrontendLoggerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ife Sunmola
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/frontend/log")
@Slf4j
public class FrontendLoggerController {
	private final FrontendLoggerService loggerService;

	@PostMapping("/debug")
	public void logDebug(@RequestBody LogBody logBody) {
		log.debug("Debug from frontend: {}", loggerService.saveToLog(logBody));
	}

	@PostMapping("/info")
	public void logInfo(@RequestBody LogBody logBody) {
		log.info("Info from frontend: {}", loggerService.saveToLog(logBody));
	}

	@PostMapping("/warn")
	public void logWarn(@RequestBody LogBody logBody) {
		log.warn("Warning from frontend: {}", loggerService.saveToLog(logBody));
	}

	@PostMapping("/error")
	public void logError(@RequestBody LogBody logBody) {
		log.error("Error from frontend: {}", loggerService.saveToLog(logBody));
	}
}