package com.example.fraga.HubSpot.port.output;

import com.example.fraga.HubSpot.adapters.output.client.TokenRequest;
import com.example.fraga.HubSpot.adapters.output.client.TokenResponse;

public interface AuthClient {
    Boolean validadeGeneratedToken(String accessToken);

    TokenResponse exchangeCodeForToken(TokenRequest tokenRequest);
}