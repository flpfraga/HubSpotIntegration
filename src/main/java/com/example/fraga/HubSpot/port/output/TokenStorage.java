package com.example.fraga.HubSpot.port.output;

import com.example.fraga.HubSpot.adapters.output.client.TokenResponse;
import com.example.fraga.HubSpot.domain.model.Token;

import java.util.Optional;

public interface TokenStorage {

    void saveTokenData(Token token);

    Optional<Token> findTokenByState(String state);

    void updateTokenAfterGenerate(TokenResponse tokenResponse, String state);
}
