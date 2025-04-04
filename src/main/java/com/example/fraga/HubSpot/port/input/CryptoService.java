package com.example.fraga.HubSpot.port.input;

public interface CryptoService {
    /**
     * Criptografa uma string usando o algoritmo AES
     * @param data String a ser criptografada
     * @return String criptografada em Base64
     */
    String encrypt(String data);

    /**
     * Descriptografa uma string previamente criptografada
     * @param encryptedData String criptografada em Base64
     * @return String descriptografada
     */
    String decrypt(String encryptedData);
} 