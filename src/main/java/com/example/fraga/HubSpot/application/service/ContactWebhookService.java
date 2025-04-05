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

    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_ERROR = "ERROR";

    private final ContactRepositoryPort contactRepository;
    private final WebhookConfig webhookConfig;

    public ContactWebhookService(ContactRepositoryPort contactRepository, WebhookConfig webhookConfig) {
        this.contactRepository = contactRepository;
        this.webhookConfig = webhookConfig;
    }

    @Override
    public void processContactCreation(Contact contact) {
        try {
            log.info("m=processContactCreation contact={}", contact);
            
            validateSecretToken(contact.getSecret());
            filterByEvent(contact.getProperties());
            
            contact.setStatus(STATUS_SUCCESS);
            contactRepository.save(contact);
            
            log.info("m=processContactCreation status=success contact={}", contact);
        } catch (Exception e) {
            log.error("m=processContactCreation status=error contact={} exception={} message={}", 
                contact, e.getClass().getSimpleName(), e.getMessage());
            contact.setStatus(STATUS_ERROR);
            contact.setErrorMessage(e.getMessage());
            contactRepository.save(contact);
            throw e;
        }
    }

    private void validateSecretToken(String receivedSecret) {
        if (!webhookConfig.getSecretToken().equals(receivedSecret)) {
            throw new SecurityException("Secret token inválido");
        }
    }

    private void filterByEvent(Map<String, Object> properties) {
        if (!properties.containsKey(webhookConfig.getEvent())) {
            throw new IllegalArgumentException("Evento não encontrado no payload");
        }
    }
} 