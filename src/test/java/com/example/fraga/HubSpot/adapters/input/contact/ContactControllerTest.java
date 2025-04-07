package com.example.fraga.HubSpot.adapters.input.contact;

import com.example.fraga.HubSpot.adapters.input.contactCreate.ContactController;
import com.example.fraga.HubSpot.adapters.input.contactCreate.ContactRequest;
import com.example.fraga.HubSpot.adapters.input.contactCreate.ContactResponse;
import com.example.fraga.HubSpot.adapters.input.models.DefaultResponse;
import com.example.fraga.HubSpot.domain.model.Contact;
import com.example.fraga.HubSpot.port.input.ContactUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactControllerTest {

    @Mock
    private ContactUseCase contactUseCase;

    @InjectMocks
    private ContactController contactController;

    private ContactRequest contactRequest;
    private ContactResponse contactResponse;

    @BeforeEach
    void setUp() {
        contactRequest = ContactRequest.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        contactResponse = ContactResponse.builder()
                .message("teste")
                .build();

    }

    @Test
    void createContact_shouldReturnContact_whenRequestIsValid() {
        // Arrange
        when(contactUseCase.createContact(any(ContactRequest.class), anyString())).thenReturn(contactResponse);

        // Act
        ResponseEntity<DefaultResponse<ContactResponse>> response =
            contactController.create("test-token", contactRequest);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(contactResponse, response.getBody().getDados());
        
        verify(contactUseCase, times(1))
            .createContact(any(ContactRequest.class), anyString());
    }

    @Test
    void createContact_shouldThrowException_whenRequestIsInvalid() {
        // Arrange
        ContactRequest invalidRequest = ContactRequest.builder()
                .email("")
                .firstName("")
                .lastName("")
                .build();

        when(contactUseCase.createContact(any(ContactRequest.class), anyString()))
                .thenThrow(new RuntimeException("Invalid request"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            contactController.create( "test-token", invalidRequest));
        
        verify(contactUseCase, times(1))
            .createContact(any(ContactRequest.class), anyString());
    }
} 