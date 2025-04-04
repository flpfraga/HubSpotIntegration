package com.example.fraga.HubSpot.port.output;

import com.example.fraga.HubSpot.domain.model.Token;
import com.example.fraga.HubSpot.infrastructure.client.TokenRequest;
import com.example.fraga.HubSpot.infrastructure.client.TokenResponse;

public interface AuthClient {
    Boolean validateCallback(String accessToken);

    TokenResponse exchangeCodeForToken(TokenRequest tokenRequest);
}