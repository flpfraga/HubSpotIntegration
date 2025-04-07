package com.example.fraga.HubSpot.domain.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final String code;
    private final String message;
    private final String details;

    protected BaseException(String code, String message, String details) {
        super(message);
        this.code = code;
        this.message = message;
        this.details = details;
    }

    protected BaseException(String code, String message) {
        this(code, message, null);
    }
} 