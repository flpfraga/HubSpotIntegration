package com.example.fraga.HubSpot.adapters.output.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenRequestTest {

    @Test
    void builder_shouldCreateTokenRequest_whenAllFieldsAreProvided() {
        // Arrange & Act
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setClientId("client-id");
        tokenRequest.setClientSecret("client-secret");
        tokenRequest.setCode("code");
        tokenRequest.setRedirectUri("http://localhost:8080/callback");

        // Assert
        assertNotNull(tokenRequest);
        assertEquals("client-id", tokenRequest.getClientId());
        assertEquals("client-secret", tokenRequest.getClientSecret());
        assertEquals("code", tokenRequest.getCode());
        assertEquals("http://localhost:8080/callback", tokenRequest.getRedirectUri());
    }

    @Test
    void builder_shouldCreateTokenRequest_whenOnlyRequiredFieldsAreProvided() {
        // Arrange & Act
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setClientId("client-id");
        tokenRequest.setClientSecret("client-secret");
        tokenRequest.setCode("code");

        // Assert
        assertNotNull(tokenRequest);
        assertEquals("client-id", tokenRequest.getClientId());
        assertEquals("client-secret", tokenRequest.getClientSecret());
        assertEquals("code", tokenRequest.getCode());
        assertNull(tokenRequest.getRedirectUri());
    }

    @Test
    void equalsAndHashCode_shouldWorkCorrectly() {
        // Arrange
        TokenRequest tokenRequest1 = new TokenRequest();
        tokenRequest1.setClientId("client-id");
        tokenRequest1.setClientSecret("client-secret");
        tokenRequest1.setCode("code");
        tokenRequest1.setRedirectUri("http://localhost:8080/callback");

        TokenRequest tokenRequest2 = new TokenRequest();
        tokenRequest2.setClientId("client-id");
        tokenRequest2.setClientSecret("client-secret");
        tokenRequest2.setCode("code");
        tokenRequest2.setRedirectUri("http://localhost:8080/callback");

        // Act & Assert
        assertEquals(tokenRequest1, tokenRequest2);
        assertEquals(tokenRequest1.hashCode(), tokenRequest2.hashCode());
    }
} 