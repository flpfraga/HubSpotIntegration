package com.example.fraga.HubSpot.domain.exception;

public class InfrastructureException extends BaseException {
    public InfrastructureException(String code, String message) {
        super(code, message);
    }

    public InfrastructureException(String code, String message, String details) {
        super(code, message, details);
    }
} 