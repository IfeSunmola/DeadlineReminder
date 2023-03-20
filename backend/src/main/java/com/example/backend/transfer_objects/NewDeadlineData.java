package com.example.backend.transfer_objects;

import java.time.Instant;
import java.util.ArrayList;

/**
 * @author Ife Sunmola
 */
public record NewDeadlineData(String title, Instant dueDate, String dueTime,
                              boolean otherPeopleSee, String email,
                              ArrayList<String> timesToRemind) {
}
