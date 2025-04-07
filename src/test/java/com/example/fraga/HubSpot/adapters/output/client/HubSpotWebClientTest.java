package com.example.fraga.HubSpot.adapters.output.client;

import com.example.fraga.HubSpot.domain.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HubSpotWebClientTest {

    @Mock
    private ExchangeFunction exchangeFunction;

    private HubSpotWebClient hubSpotWebClient;

    @BeforeEach
    void setUp() {
        WebClient webClient = WebClient.builder()
                .exchangeFunction(exchangeFunction)
                .build();

        hubSpotWebClient = new HubSpotWebClient(webClient);
        ReflectionTestUtils.setField(hubSpotWebClient, "validationUri", "/oauth/v1/access-tokens");
    }

    @Test
    void validadeGeneratedToken_shouldReturnTrue_whenTokenIsValid() {
        // Arrange
        String accessToken = "valid-token";
        ClientResponse response = ClientResponse.create(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body("{}")
                .build();

        when(exchangeFunction.exchange(any()))
                .thenReturn(Mono.just(response));

        // Act
        Boolean result = hubSpotWebClient.validadeGeneratedToken(accessToken);

        // Assert
        assertTrue(result);
    }

    @Test
    void exchangeCodeForToken_shouldReturnTokenResponse_whenCodeIsValid() {
        // Arrange
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setClientId("client-id");
        tokenRequest.setClientSecret("client-secret");
        tokenRequest.setCode("code");
        tokenRequest.setRedirectUri("http://localhost:8080/callback");

        TokenResponse expectedResponse = new TokenResponse();
        expectedResponse.setAccess_token("access-token");
        expectedResponse.setRefresh_token("refresh-token");
        expectedResponse.setExpires_in("3600");
        expectedResponse.setToken_type("Bearer");

        ClientResponse response = ClientResponse.create(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body("{\"access_token\":\"access-token\",\"refresh_token\":\"refresh-token\",\"expires_in\":\"3600\",\"token_type\":\"Bearer\"}")
                .build();

        when(exchangeFunction.exchange(any()))
                .thenReturn(Mono.just(response));

        // Act
        TokenResponse result = hubSpotWebClient.exchangeCodeForToken(tokenRequest);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.getAccess_token(), result.getAccess_token());
        assertEquals(expectedResponse.getRefresh_token(), result.getRefresh_token());
        assertEquals(expectedResponse.getExpires_in(), result.getExpires_in());
        assertEquals(expectedResponse.getToken_type(), result.getToken_type());
    }

    @Test
    void fallbackValidateCallback_shouldThrowBusinessException() {
        // Arrange
        String accessToken = "token";
        Throwable ex = new RuntimeException("Error");

        // Act & Assert
        assertThrows(BusinessException.class, () ->
            hubSpotWebClient.fallbackValidateCallback(accessToken, ex));
    }

    @Test
    void fallbackExchangeCodeForToken_shouldThrowBusinessException() {
        // Arrange
        TokenRequest tokenRequest = new TokenRequest();
        Throwable ex = new RuntimeException("Error");

        // Act & Assert
        assertThrows(BusinessException.class, () ->
            hubSpotWebClient.fallbackExchangeCodeForToken(tokenRequest, ex));
    }
} 