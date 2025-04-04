package com.example.fraga.HubSpot.port.output;

import com.example.fraga.HubSpot.domain.model.Token;

public interface TokenRepositoryPort {
    Token findByEncryptedToken(String encryptedToken);
    void save(Token token);
} 