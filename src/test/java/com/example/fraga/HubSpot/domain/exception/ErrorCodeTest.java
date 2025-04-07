package com.example.fraga.HubSpot.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorCodeTest {

    @Test
    void getCode_shouldReturnCode_whenErrorCodeIsValid() {
        // Arrange
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;

        // Act
        String code = errorCode.getCode();

        // Assert
        assertEquals("INVALID_REQUEST", code);
    }

    @Test
    void getMessage_shouldReturnMessage_whenErrorCodeIsValid() {
        // Arrange
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;

        // Act
        String message = errorCode.getMessage();

        // Assert
        assertEquals("Requisição inválida", message);
    }

    @Test
    void values_shouldReturnAllErrorCodes() {
        // Act
        ErrorCode[] errorCodes = ErrorCode.values();

        // Assert
        assertEquals(13, errorCodes.length);
        assertTrue(containsErrorCode(errorCodes, ErrorCode.INVALID_REQUEST));
        assertTrue(containsErrorCode(errorCodes, ErrorCode.INVALID_SECRET_TOKEN));
        assertTrue(containsErrorCode(errorCodes, ErrorCode.INVALID_EVENT));
    }

    private boolean containsErrorCode(ErrorCode[] errorCodes, ErrorCode target) {
        for (ErrorCode code : errorCodes) {
            if (code == target) {
                return true;
            }
        }
        return false;
    }
} 