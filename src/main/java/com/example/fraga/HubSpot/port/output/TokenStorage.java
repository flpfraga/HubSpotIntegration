package com.example.fraga.HubSpot.port.output;

import com.example.fraga.HubSpot.domain.model.Token;

import java.util.Optional;

public interface TokenStorage {

    void saveTokenData(Token token);

    Optional<Token> findTokenById(String state);

    Token updateAccessToken(String state, String accessToken);
}
