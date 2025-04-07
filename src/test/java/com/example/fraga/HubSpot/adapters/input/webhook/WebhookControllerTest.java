package com.example.fraga.HubSpot.adapters.input.webhook;

import com.example.fraga.HubSpot.adapters.input.contactWebhook.ContactWebhookController;
import com.example.fraga.HubSpot.adapters.input.contactWebhook.ContactWebhookResponse;
import com.example.fraga.HubSpot.adapters.input.models.DefaultResponse;
import com.example.fraga.HubSpot.domain.model.Contact;
import com.example.fraga.HubSpot.port.input.ContactWebhookUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebhookControllerTest {

    @Mock
    private ContactWebhookUseCase contactWebhookUseCase;

    @InjectMocks
    private ContactWebhookController contactWebhookController;

    private ContactWebhookResponse contactWebhookResponse;

    @BeforeEach
    void setUp() {
        contactWebhookResponse = ContactWebhookResponse.builder()
                .message("teste")
                .build();
    }

    @Test
    void handleContactCreation_shouldReturnContact_whenRequestIsValid() {
        Map<String, Contact> eventContact = Map.of();
        // Arrange
        when(contactWebhookUseCase.processContactCreation(anyMap(), anyString()))
                .thenReturn(contactWebhookResponse);

        // Act
        ResponseEntity<DefaultResponse<ContactWebhookResponse>> response =
            contactWebhookController.handleContactCreation("teste", eventContact);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        verify(contactWebhookUseCase, times(1))
            .processContactCreation(eventContact, "teste");
    }

} 