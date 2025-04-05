package com.example.fraga.HubSpot.application.service;

import com.example.fraga.HubSpot.config.WebhookConfig;
import com.example.fraga.HubSpot.domain.model.Contact;
import com.example.fraga.HubSpot.port.input.ContactWebhookUseCase;
import com.example.fraga.HubSpot.port.output.ContactRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ContactWebhookService implements ContactWebhookUseCase {

    private final ContactRepositoryPort contactRepository;
    private final WebhookConfig webhookConfig;

    public ContactWebhookService(ContactRepositoryPort contactRepository, WebhookConfig webhookConfig) {
        this.contactRepository = contactRepository;
        this.webhookConfig = webhookConfig;
    }

    @Override
    public void processContactCreation(Map<String, Contact> eventContact,String secret) {
        try {
            filterByEvent(eventContact);
        } catch (Exception e) {
            throw e;
        }
    }

    private Set<Contact> filterByEvent(Map<String, Contact> eventContact) {
        return eventContact.entrySet().stream().filter(
                event -> webhookConfig.getEvent().equalsIgnoreCase(event.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }
} 