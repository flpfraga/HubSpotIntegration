package com.example.fraga.HubSpot.application.service;

import com.example.fraga.HubSpot.domain.model.Token;
import com.example.fraga.HubSpot.infrastructure.security.crypto.AESCryptoSecurity;
import com.example.fraga.HubSpot.port.input.TokenValidationUseCase;
import com.example.fraga.HubSpot.port.output.TokenRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class TokenValidationService implements TokenValidationUseCase {

    private final TokenRepositoryPort tokenRepository;
    private final AESCryptoSecurity cryptoService;

    public TokenValidationService(TokenRepositoryPort tokenRepository, AESCryptoSecurity cryptoService) {
        this.tokenRepository = tokenRepository;
        this.cryptoService = cryptoService;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            String encryptedToken = cryptoService.encrypt(token);
            Token storedToken = tokenRepository.findByEncryptedToken(encryptedToken);
            
            if (storedToken == null || storedToken.isExpired()) {
                return false;
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
} 