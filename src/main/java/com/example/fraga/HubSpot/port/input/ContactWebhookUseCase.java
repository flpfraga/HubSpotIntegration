package com.example.fraga.HubSpot.port.input;

import com.example.fraga.HubSpot.domain.model.Contact;

import java.util.Map;

public interface ContactWebhookUseCase {
    void processContactCreation(Map<String, Contact> eventContact, String secret);
} 