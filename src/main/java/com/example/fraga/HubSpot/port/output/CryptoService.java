package com.example.fraga.HubSpot.port.output;

import org.springframework.stereotype.Service;

@Service
public interface CryptoService {
    String encrypt(String plainText);
    String decrypt(String encryptedText);
}
