package com.example.fraga.HubSpot.port.input;

import com.example.fraga.HubSpot.adapters.input.auth.AuthRequest;
import com.example.fraga.HubSpot.adapters.input.auth.AuthResponse;

public interface AuthHubSpotUseCase {

    AuthResponse generateAuthorizationUrl(AuthRequest request);

    AuthResponse handleCallback(String code, String state);

    void validateToken(String code, String state);
}