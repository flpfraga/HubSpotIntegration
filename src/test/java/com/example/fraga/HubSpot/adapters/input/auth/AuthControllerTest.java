package com.example.fraga.HubSpot.adapters.input.auth;

import com.example.fraga.HubSpot.adapters.input.models.DefaultResponse;
import com.example.fraga.HubSpot.port.input.AuthHubSpotUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthHubSpotUseCase authHubSpotUseCase;

    @InjectMocks
    private AuthController authController;

    private AuthRequest authRequest;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        authRequest = AuthRequest.builder()
                .clientId("test-client-id")
                .scope("test-scope")
                .state("test-state")
                .clientSecret("test-secret")
                .build();

        authResponse = AuthResponse.builder()
                .authorizationUrl("https://test-url")
                .state("test-state")
                .accessToken("test-token")
                .refreshToken("test-refresh-token")
                .expiresIn(3600L)
                .build();
    }

    @Test
    void generateAuthorizationUrl_shouldReturnAuthResponse_whenRequestIsValid() {
        // Arrange
        when(authHubSpotUseCase.generateAuthorizationUrl(any(AuthRequest.class)))
                .thenReturn(authResponse);

        // Act
        ResponseEntity<DefaultResponse<AuthResponse>> response = 
            authController.generateAuthorizationUrl(authRequest);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(authResponse, response.getBody().getDados());
        
        verify(authHubSpotUseCase, times(1))
            .generateAuthorizationUrl(any(AuthRequest.class));
    }

    @Test
    void handleCallback_shouldReturnSuccessView_whenCodeAndStateAreValid() {
        // Arrange
        String code = "test-code";
        String state = "test-state";
        when(authHubSpotUseCase.handleCallback(code, state))
                .thenReturn(authResponse);

        // Act
        var modelAndView = authController.handleCallback(code, state);

        // Assert
        assertNotNull(modelAndView);
        assertEquals("auth/success", modelAndView.getViewName());
        assertEquals(authResponse, modelAndView.getModel().get("token"));
        
        verify(authHubSpotUseCase, times(1))
            .handleCallback(code, state);
    }

    @Test
    void handleCallback_shouldReturnErrorView_whenExceptionOccurs() {
        // Arrange
        String code = "test-code";
        String state = "test-state";
        when(authHubSpotUseCase.handleCallback(code, state))
                .thenThrow(new RuntimeException("Test error"));

        // Act
        var modelAndView = authController.handleCallback(code, state);

        // Assert
        assertNotNull(modelAndView);
        assertEquals("auth/error", modelAndView.getViewName());
        
        verify(authHubSpotUseCase, times(1))
            .handleCallback(code, state);
    }
} 