package com.example.fraga.HubSpot.adapters.output.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenResponseTest {

    @Test
    void builder_shouldCreateTokenResponse_whenAllFieldsAreProvided() {
        // Arrange & Act
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccess_token("access-token");
        tokenResponse.setRefresh_token("refresh-token");
        tokenResponse.setExpires_in("3600");
        tokenResponse.setToken_type("Bearer");

        // Assert
        assertNotNull(tokenResponse);
        assertEquals("access-token", tokenResponse.getAccess_token());
        assertEquals("refresh-token", tokenResponse.getRefresh_token());
        assertEquals("3600", tokenResponse.getExpires_in());
        assertEquals("Bearer", tokenResponse.getToken_type());
    }

    @Test
    void builder_shouldCreateTokenResponse_whenOnlyRequiredFieldsAreProvided() {
        // Arrange & Act
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccess_token("access-token");

        // Assert
        assertNotNull(tokenResponse);
        assertEquals("access-token", tokenResponse.getAccess_token());
        assertNull(tokenResponse.getRefresh_token());
        assertNull(tokenResponse.getExpires_in());
        assertNull(tokenResponse.getToken_type());
    }

    @Test
    void equalsAndHashCode_shouldWorkCorrectly() {
        // Arrange
        TokenResponse tokenResponse1 = new TokenResponse();
        tokenResponse1.setAccess_token("access-token");
        tokenResponse1.setRefresh_token("refresh-token");
        tokenResponse1.setExpires_in("3600");
        tokenResponse1.setToken_type("Bearer");

        TokenResponse tokenResponse2 = new TokenResponse();
        tokenResponse2.setAccess_token("access-token");
        tokenResponse2.setRefresh_token("refresh-token");
        tokenResponse2.setExpires_in("3600");
        tokenResponse2.setToken_type("Bearer");

        // Act & Assert
        assertEquals(tokenResponse1, tokenResponse2);
        assertEquals(tokenResponse1.hashCode(), tokenResponse2.hashCode());
    }
} 