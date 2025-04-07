package com.example.fraga.HubSpot.port.input;

import com.example.fraga.HubSpot.adapters.input.contactWebhook.ContactWebhookResponse;
import com.example.fraga.HubSpot.domain.model.Contact;

import java.util.Map;

public interface ContactWebhookUseCase {
    ContactWebhookResponse processContactCreation(Map<String, Contact> eventContact, String secret);
} 