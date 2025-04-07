package com.example.fraga.HubSpot.adapters.input.models;

import com.example.fraga.HubSpot.adapters.input.contactCreate.ContactRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactRequestTest {

    @Test
    void builder_shouldCreateContactRequest_whenAllFieldsAreProvided() {
        // Act
        ContactRequest request = ContactRequest.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        // Assert
        assertNotNull(request);
        assertEquals("test@example.com", request.getEmail());
        assertEquals("Test", request.getFirstName());
        assertEquals("User", request.getLastName());
    }

    @Test
    void builder_shouldCreateContactRequest_whenOnlyRequiredFieldsAreProvided() {
        // Act
        ContactRequest request = ContactRequest.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        // Assert
        assertNotNull(request);
        assertEquals("test@example.com", request.getEmail());
        assertEquals("Test", request.getFirstName());
        assertEquals("User", request.getLastName());
    }

    @Test
    void equalsAndHashCode_shouldWorkCorrectly() {
        // Arrange
        ContactRequest request1 = ContactRequest.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        ContactRequest request2 = ContactRequest.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        ContactRequest request3 = ContactRequest.builder()
                .email("different@example.com")
                .firstName("Different")
                .lastName("User")
                .build();

        // Assert
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }
} 