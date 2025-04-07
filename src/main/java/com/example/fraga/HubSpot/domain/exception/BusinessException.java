package com.example.fraga.HubSpot.domain.exception;

public class BusinessException extends BaseException {
    public BusinessException(String code, String message) {
        super(code, message);
    }

    public BusinessException(String code, String message, String details) {
        super(code, message, details);
    }
} 