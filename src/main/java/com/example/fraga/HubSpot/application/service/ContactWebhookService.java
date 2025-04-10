package com.example.fraga.HubSpot.application.service;

import com.example.fraga.HubSpot.adapters.input.contactWebhook.ContactWebhookResponse;
import com.example.fraga.HubSpot.config.WebhookConfig;
import com.example.fraga.HubSpot.domain.exception.ErrorCode;
import com.example.fraga.HubSpot.domain.exception.InfrastructureException;
import com.example.fraga.HubSpot.domain.exception.ValidationException;
import com.example.fraga.HubSpot.domain.model.Contact;
import com.example.fraga.HubSpot.port.input.ContactWebhookUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ContactWebhookService implements ContactWebhookUseCase {

    private final WebhookConfig webhookConfig;

    public ContactWebhookService(WebhookConfig webhookConfig) {
        this.webhookConfig = webhookConfig;
    }

    @Override
    public ContactWebhookResponse processContactCreation(Map<String, Contact> eventContact, String secret) {
        try {
            validateSecretToken(secret);
            Set<Contact> validEvents = filterByEvent(eventContact);
            validEvents.forEach(System.out::println);
            log.info("m=processContactCreation status=success");
            return new ContactWebhookResponse("Contratos processaods");
        } catch (RedisConnectionFailureException | SerializationException e) {
            logError(e);
            throw new InfrastructureException(ErrorCode.DATABASE_ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            logError(e);
            throw new InfrastructureException(
                    ErrorCode.INTERNAL_ERROR.getCode(), e.getMessage()
            );
        }
    }

    private void logError(Exception e) {
        log.error("m=processContactCreation status=error exception={} message={}",
                e.getClass().getSimpleName(), e.getMessage());
    }

    private void validateSecretToken(String secret) {
        if (!webhookConfig.getSecretToken().equals(secret)) {
            throw new ValidationException(
                    ErrorCode.INVALID_SECRET_TOKEN.getCode(),
                    ErrorCode.INVALID_SECRET_TOKEN.getMessage()
            );
        }
    }

    private Set<Contact>  filterByEvent(Map<String, Contact> eventContact) {
        return eventContact.entrySet().stream()
                .filter(event -> webhookConfig.getEvent().equalsIgnoreCase(event.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }
} 