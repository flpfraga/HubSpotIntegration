package com.example.fraga.HubSpot.port.output;

public interface CryptoService {
    String encrypt(String plainText);
    String decrypt(String encryptedText);
}
