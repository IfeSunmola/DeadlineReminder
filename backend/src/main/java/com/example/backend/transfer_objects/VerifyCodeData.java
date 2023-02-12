package com.example.backend.transfer_objects;

/**
 * @author Ife Sunmola
 */
public record VerifyCodeData(Long codeId, String userEmail, String codeFromUser) {
}