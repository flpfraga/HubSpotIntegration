package com.example.fraga.HubSpot.adapters.input.models;

import com.example.fraga.HubSpot.adapters.input.auth.AuthResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void builder_shouldCreateAuthResponse_whenAllFieldsAreProvided() {
        // Act
        AuthResponse response = AuthResponse.builder()
                .authorizationUrl("https://test-url")
                .state("test-state")
                .accessToken("test-token")
                .refreshToken("test-refresh-token")
                .expiresIn(3600L)
                .build();

        // Assert
        assertNotNull(response);
        assertEquals("https://test-url", response.getAuthorizationUrl());
        assertEquals("test-state", response.getState());
        assertEquals("test-token", response.getAccessToken());
        assertEquals("test-refresh-token", response.getRefreshToken());
        assertEquals(3600L, response.getExpiresIn());
    }

    @Test
    void builder_shouldCreateAuthResponse_whenOnlyRequiredFieldsAreProvided() {
        // Act
        AuthResponse response = AuthResponse.builder()
                .authorizationUrl("https://test-url")
                .state("test-state")
                .build();

        // Assert
        assertNotNull(response);
        assertEquals("https://test-url", response.getAuthorizationUrl());
        assertEquals("test-state", response.getState());
        assertNull(response.getAccessToken());
        assertNull(response.getRefreshToken());
        assertNull(response.getExpiresIn());
    }

    @Test
    void equalsAndHashCode_shouldWorkCorrectly() {
        // Arrange
        AuthResponse response1 = AuthResponse.builder()
                .authorizationUrl("https://test-url")
                .state("test-state")
                .accessToken("test-token")
                .refreshToken("test-refresh-token")
                .expiresIn(3600L)
                .build();

        AuthResponse response2 = AuthResponse.builder()
                .authorizationUrl("https://test-url")
                .state("test-state")
                .accessToken("test-token")
                .refreshToken("test-refresh-token")
                .expiresIn(3600L)
                .build();

        AuthResponse response3 = AuthResponse.builder()
                .authorizationUrl("https://different-url")
                .state("different-state")
                .accessToken("different-token")
                .refreshToken("different-refresh-token")
                .expiresIn(1800L)
                .build();

        // Assert
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }
} 