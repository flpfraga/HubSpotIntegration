package com.example.fraga.HubSpot.domain.exception;

public class ValidationException extends BusinessException {
    public ValidationException(String code, String message) {
        super(code, message);
    }

    public ValidationException(String code, String message, String details) {
        super(code, message, details);
    }
} 