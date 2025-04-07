package com.example.fraga.HubSpot.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    @Test
    void builder_shouldCreateContact_whenAllFieldsAreProvided() {
        // Act
        Contact contact = Contact.builder()
                .id("test-id")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        // Assert
        assertNotNull(contact);
        assertEquals("test-id", contact.getId());
        assertEquals("test@example.com", contact.getEmail());
        assertEquals("Test", contact.getFirstName());
        assertEquals("User", contact.getLastName());

    }

    @Test
    void builder_shouldCreateContact_whenOnlyRequiredFieldsAreProvided() {
        // Act
        Contact contact = Contact.builder()
                .id("test-id")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        // Assert
        assertNotNull(contact);
        assertEquals("test-id", contact.getId());
        assertEquals("test@example.com", contact.getEmail());
        assertEquals("Test", contact.getFirstName());
        assertEquals("User", contact.getLastName());
    }

    @Test
    void equalsAndHashCode_shouldWorkCorrectly() {
        // Arrange
        Contact contact1 = Contact.builder()
                .id("test-id")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        Contact contact2 = Contact.builder()
                .id("test-id")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        Contact contact3 = Contact.builder()
                .id("different-id")
                .email("different@example.com")
                .firstName("Different")
                .lastName("User")
                .build();

        // Assert
        assertEquals(contact1, contact2);
        assertNotEquals(contact1, contact3);
        assertEquals(contact1.hashCode(), contact2.hashCode());
        assertNotEquals(contact1.hashCode(), contact3.hashCode());
    }
} 