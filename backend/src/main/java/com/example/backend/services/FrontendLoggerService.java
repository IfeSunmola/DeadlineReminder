package com.example.backend.services;

import com.example.backend.models.LogBody;
import com.example.backend.repos.FrontEndLoggerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Ife Sunmola
 */
@Service
@RequiredArgsConstructor
public class FrontendLoggerService {
	private final FrontEndLoggerRepo loggerRepo;

	public void saveToLog(LogBody logBody) {
		loggerRepo.save(logBody);
	}
}