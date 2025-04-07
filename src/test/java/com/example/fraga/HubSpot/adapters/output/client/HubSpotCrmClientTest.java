package com.example.fraga.HubSpot.adapters.output.client;

import com.example.fraga.HubSpot.domain.exception.BusinessException;
import com.example.fraga.HubSpot.domain.model.Contact;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HubSpotCrmClientTest {

    @Mock
    private ExchangeFunction exchangeFunction;

    private HubSpotCrmClient hubSpotCrmClient;

    @BeforeEach
    void setUp() {
        WebClient webClient = WebClient.builder()
                .exchangeFunction(exchangeFunction)
                .build();

        hubSpotCrmClient = new HubSpotCrmClient(webClient);
    }

    @Test
    void create_shouldReturnContact_whenContactIsCreatedSuccessfully() throws Exception {
        // Arrange
        Contact contact = Contact.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        String accessToken = "valid-token";

        String jsonResponse = "{\"id\":\"123\",\"properties\":{\"email\":\"test@example.com\",\"firstname\":\"Test\",\"lastname\":\"User\"}}";
        JsonNode responseNode = new ObjectMapper().readTree(jsonResponse);

        ClientResponse response = ClientResponse.create(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(jsonResponse)
                .build();

        when(exchangeFunction.exchange(any()))
                .thenReturn(Mono.just(response));

        // Act
        Contact result = hubSpotCrmClient.create(contact, accessToken);

        // Assert
        assertNotNull(result);
        assertEquals(contact.getEmail(), result.getEmail());
        assertEquals(contact.getFirstName(), result.getFirstName());
        assertEquals(contact.getLastName(), result.getLastName());
    }

    @Test
    void create_shouldRetry_whenRateLimitIsReached() {
        // Arrange
        Contact contact = Contact.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        String accessToken = "valid-token";

        ClientResponse rateLimitResponse = ClientResponse.create(HttpStatus.TOO_MANY_REQUESTS)
                .header("Retry-After", "5")
                .build();

        ClientResponse successResponse = ClientResponse.create(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body("{\"id\":\"123\",\"properties\":{\"email\":\"test@example.com\",\"firstname\":\"Test\",\"lastname\":\"User\"}}")
                .build();

        when(exchangeFunction.exchange(any()))
                .thenReturn(Mono.just(rateLimitResponse))
                .thenReturn(Mono.just(successResponse));

        // Act
        Contact result = hubSpotCrmClient.create(contact, accessToken);

        // Assert
        assertNotNull(result);
        assertEquals(contact.getEmail(), result.getEmail());
        assertEquals(contact.getFirstName(), result.getFirstName());
        assertEquals(contact.getLastName(), result.getLastName());
    }

    @Test
    void fallbackCreate_shouldThrowBusinessException() {
        // Arrange
        Contact contact = Contact.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        String accessToken = "valid-token";
        Throwable ex = new RuntimeException("Error");

        // Act & Assert
        assertThrows(BusinessException.class, () ->
            hubSpotCrmClient.fallbackCreate(contact, accessToken, ex));
    }
} 