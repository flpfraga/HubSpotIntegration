package com.example.fraga.HubSpot.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {

    @Test
    void constructor_shouldSetCodeAndMessage_whenErrorCodeIsProvided() {
        // Arrange
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;

        // Act
        BusinessException exception = new BusinessException(errorCode.getCode(), "Requisição inválida");

        // Assert
        assertEquals(errorCode.getCode(), exception.getCode());
        assertEquals(errorCode.getMessage(), exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructor_shouldSetCodeAndMessage_whenCodeAndMessageAreProvided() {
        // Arrange
        String code = "TEST_CODE";
        String message = "Test message";

        // Act
        BusinessException exception = new BusinessException(code, message);

        // Assert
        assertEquals(code, exception.getCode());
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructor_shouldSetCodeMessageAndCause_whenAllParametersAreProvided() {
        // Arrange
        String code = "TEST_CODE";
        String message = "Test message";

        // Act
        BusinessException exception = new BusinessException(code, message);

        // Assert
        assertEquals(code, exception.getCode());
        assertEquals(message, exception.getMessage());
    }
} 