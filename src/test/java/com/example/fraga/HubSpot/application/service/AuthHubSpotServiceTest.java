package com.example.fraga.HubSpot.application.service;

import com.example.fraga.HubSpot.adapters.input.auth.AuthRequest;
import com.example.fraga.HubSpot.adapters.input.auth.AuthResponse;
import com.example.fraga.HubSpot.adapters.output.client.TokenRequest;
import com.example.fraga.HubSpot.adapters.output.client.TokenResponse;
import com.example.fraga.HubSpot.domain.exception.BusinessException;
import com.example.fraga.HubSpot.domain.model.Token;
import com.example.fraga.HubSpot.port.output.AuthClient;
import com.example.fraga.HubSpot.port.output.CryptoService;
import com.example.fraga.HubSpot.port.output.TokenStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthHubSpotServiceTest {

    @Mock
    private AuthClient authClient;

    @Mock
    private TokenStorage tokenStorage;

    @Mock
    private CryptoService cryptoService;

    @Mock
    private ModelMapper mapper;

    private AuthHubSpotService authHubSpotService;

    @BeforeEach
    void setUp() {
        authHubSpotService = new AuthHubSpotService(authClient, tokenStorage, cryptoService, mapper);
        ReflectionTestUtils.setField(authHubSpotService, "redirectUri", "http://localhost:8080/callback");
        ReflectionTestUtils.setField(authHubSpotService, "authorizationUrl", "https://app.hubspot.com/oauth/authorize");
    }

    @Test
    void generateAuthorizationUrl_shouldReturnAuthResponse_whenRequestIsValid() {
        // Arrange
        AuthRequest request = AuthRequest.builder()
                .clientId("client-id")
                .clientSecret("client-secret")
                .scope("contacts")
                .state("state")
                .build();

        when(cryptoService.encrypt(request.getClientSecret())).thenReturn("encrypted-secret");
        doNothing().when(tokenStorage).saveTokenData(any(Token.class));

        // Act
        AuthResponse response = authHubSpotService.generateAuthorizationUrl(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.getAuthorizationUrl().contains("client_id=client-id"));
        assertTrue(response.getAuthorizationUrl().contains("scope=contacts"));
        assertTrue(response.getAuthorizationUrl().contains("state=state"));
        assertEquals("state", response.getState());
        verify(cryptoService).encrypt(request.getClientSecret());
        verify(tokenStorage).saveTokenData(any(Token.class));
    }

    @Test
    void handleCallback_shouldReturnAuthResponse_whenCodeAndStateAreValid() {
        // Arrange
        String code = "code";
        String state = "state";
        Token token = new Token("client-id", state, "encrypted-secret");
        TokenRequest tokenRequest = new TokenRequest();
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccess_token("access-token");
        tokenResponse.setRefresh_token("refresh-token");
        tokenResponse.setExpires_in("3600");

        when(tokenStorage.findTokenByState(state)).thenReturn(Optional.of(token));
        when(mapper.map(token, TokenRequest.class)).thenReturn(tokenRequest);
        when(authClient.exchangeCodeForToken(tokenRequest)).thenReturn(tokenResponse);
        when(authClient.validadeGeneratedToken(tokenResponse.getAccess_token())).thenReturn(true);

        // Act
        AuthResponse response = authHubSpotService.handleCallback(code, state);

        // Assert
        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertEquals(3600L, response.getExpiresIn());
        verify(tokenStorage).findTokenByState(state);
        verify(authClient).exchangeCodeForToken(tokenRequest);
        verify(authClient).validadeGeneratedToken(tokenResponse.getAccess_token());
    }

    @Test
    void handleCallback_shouldThrowBusinessException_whenStateIsInvalid() {
        // Arrange
        String code = "code";
        String state = "invalid-state";

        when(tokenStorage.findTokenByState(state)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () ->
            authHubSpotService.handleCallback(code, state));
    }

    @Test
    void handleCallback_shouldThrowBusinessException_whenTokenIsInvalid() {
        // Arrange
        String code = "code";
        String state = "state";
        Token token = new Token("client-id", state, "encrypted-secret");
        TokenRequest tokenRequest = new TokenRequest();
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccess_token("access-token");

        when(tokenStorage.findTokenByState(state)).thenReturn(Optional.of(token));
        when(cryptoService.decrypt(token.getClientSecret())).thenReturn("client-secret");
        when(mapper.map(token, TokenRequest.class)).thenReturn(tokenRequest);
        when(authClient.exchangeCodeForToken(tokenRequest)).thenReturn(tokenResponse);
        when(authClient.validadeGeneratedToken(tokenResponse.getAccess_token())).thenReturn(false);

        // Act & Assert
        assertThrows(BusinessException.class, () ->
            authHubSpotService.handleCallback(code, state));
    }
} 