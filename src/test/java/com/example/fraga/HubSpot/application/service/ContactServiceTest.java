package com.example.fraga.HubSpot.application.service;

import com.example.fraga.HubSpot.adapters.input.contactCreate.ContactRequest;
import com.example.fraga.HubSpot.adapters.input.contactCreate.ContactResponse;
import com.example.fraga.HubSpot.domain.exception.InfrastructureException;
import com.example.fraga.HubSpot.domain.model.Contact;
import com.example.fraga.HubSpot.port.output.ContactRepositoryPort;
import com.example.fraga.HubSpot.port.output.CrmClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private CrmClient crmClient;

    @Mock
    private ContactRepositoryPort contactRepository;

    @Mock
    private ModelMapper mapper;

    private ContactService contactService;

    @BeforeEach
    void setUp() {
        contactService = new ContactService(crmClient, contactRepository, mapper);
    }

    @Test
    void createContact_shouldReturnResponse_whenContactIsCreatedSuccessfully() {
        // Arrange
        ContactRequest request = ContactRequest.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        Contact contact = Contact.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        when(mapper.map(request, Contact.class)).thenReturn(contact);
        when(contactRepository.save(contact)).thenReturn(contact);

        // Act
        ContactResponse response = contactService.createContact(request, "valid-token");

        // Assert
        assertNotNull(response);
        assertEquals("Contato criado", response.getMessage());
        verify(crmClient).create(contact, "valid-token");
        verify(contactRepository).save(contact);
    }

    @Test
    void createContact_shouldThrowInfrastructureException_whenRedisError() {
        // Arrange
        ContactRequest request = ContactRequest.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        Contact contact = Contact.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        when(mapper.map(request, Contact.class)).thenReturn(contact);
        when(contactRepository.save(contact)).thenThrow(new RedisConnectionFailureException("Redis error"));

        // Act & Assert
        assertThrows(InfrastructureException.class, () ->
            contactService.createContact(request, "valid-token"));
    }

    @Test
    void createContact_shouldThrowInfrastructureException_whenWebClientError() {
        // Arrange
        ContactRequest request = ContactRequest.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        Contact contact = Contact.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        when(mapper.map(request, Contact.class)).thenReturn(contact);
        when(crmClient.create(contact, "valid-token"))
                .thenThrow(new WebClientResponseException(400, "Bad Request", null, null, null));

        // Act & Assert
        assertThrows(InfrastructureException.class, () ->
            contactService.createContact(request, "valid-token"));
    }
} 