package com.example.fraga.HubSpot.adapters.input.models;

import com.example.fraga.HubSpot.adapters.input.auth.AuthRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthRequestTest {

    @Test
    void builder_shouldCreateAuthRequest_whenAllFieldsAreProvided() {
        // Act
        AuthRequest request = AuthRequest.builder()
                .clientId("test-client-id")
                .scope("test-scope")
                .state("test-state")
                .clientSecret("test-secret")
                .build();

        // Assert
        assertNotNull(request);
        assertEquals("test-client-id", request.getClientId());
        assertEquals("test-scope", request.getScope());
        assertEquals("test-state", request.getState());
        assertEquals("test-secret", request.getClientSecret());
    }

    @Test
    void builder_shouldCreateAuthRequest_whenOnlyRequiredFieldsAreProvided() {
        // Act
        AuthRequest request = AuthRequest.builder()
                .clientId("test-client-id")
                .scope("test-scope")
                .state("test-state")
                .clientSecret("test-secret")
                .build();

        // Assert
        assertNotNull(request);
        assertEquals("test-client-id", request.getClientId());
        assertEquals("test-scope", request.getScope());
        assertEquals("test-state", request.getState());
        assertEquals("test-secret", request.getClientSecret());
    }

    @Test
    void equalsAndHashCode_shouldWorkCorrectly() {
        // Arrange
        AuthRequest request1 = AuthRequest.builder()
                .clientId("test-client-id")
                .scope("test-scope")
                .state("test-state")
                .clientSecret("test-secret")
                .build();

        AuthRequest request2 = AuthRequest.builder()
                .clientId("test-client-id")
                .scope("test-scope")
                .state("test-state")
                .clientSecret("test-secret")
                .build();

        AuthRequest request3 = AuthRequest.builder()
                .clientId("different-client-id")
                .scope("different-scope")
                .state("different-state")
                .clientSecret("different-secret")
                .build();

        // Assert
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }
} 