package com.example.fraga.HubSpot.application.service;

import com.example.fraga.HubSpot.adapters.input.contactWebhook.ContactWebhookResponse;
import com.example.fraga.HubSpot.config.WebhookConfig;
import com.example.fraga.HubSpot.domain.exception.InfrastructureException;
import com.example.fraga.HubSpot.domain.exception.ValidationException;
import com.example.fraga.HubSpot.domain.model.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactWebhookServiceTest {

    @Mock
    private WebhookConfig webhookConfig;

    private ContactWebhookService contactWebhookService;

    @BeforeEach
    void setUp() {
        contactWebhookService = new ContactWebhookService(webhookConfig);
    }

    @Test
    void processContactCreation_shouldReturnResponse_whenValidSecretAndEvent() {
        // Arrange
        String validSecret = "valid-secret";
        String eventType = "contact.creation";
        Map<String, Contact> eventContact = new HashMap<>();
        eventContact.put(eventType, Contact.builder().id("1").build());

        when(webhookConfig.getSecretToken()).thenReturn(validSecret);
        when(webhookConfig.getEvent()).thenReturn(eventType);

        // Act
        ContactWebhookResponse response = contactWebhookService.processContactCreation(eventContact, validSecret);

        // Assert
        assertNotNull(response);
        assertEquals("Contratos processaods", response.getMessage());
    }

    @Test
    void processContactCreation_shouldThrowValidationException_whenInvalidSecret() {
        // Arrange
        String invalidSecret = "invalid-secret";
        String validSecret = "valid-secret";
        Map<String, Contact> eventContact = new HashMap<>();

        when(webhookConfig.getSecretToken()).thenReturn(validSecret);

        // Act & Assert
        assertThrows(InfrastructureException.class, () ->
            contactWebhookService.processContactCreation(eventContact, invalidSecret));
    }

    @Test
    void processContactCreation_shouldFilterEvents_whenMultipleEvents() {
        // Arrange
        String validSecret = "valid-secret";
        String eventType = "contact.creation";
        Map<String, Contact> eventContact = new HashMap<>();
        eventContact.put(eventType, Contact.builder().id("1").build());
        eventContact.put("other.event", Contact.builder().id("2").build());

        when(webhookConfig.getSecretToken()).thenReturn(validSecret);
        when(webhookConfig.getEvent()).thenReturn(eventType);

        // Act
        ContactWebhookResponse response = contactWebhookService.processContactCreation(eventContact, validSecret);

        // Assert
        assertNotNull(response);
        assertEquals("Contratos processaods", response.getMessage());
    }
} 