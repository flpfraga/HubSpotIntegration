package com.example.fraga.HubSpot.application.service;

import com.example.fraga.HubSpot.adapters.input.contactCreate.ContactRequest;
import com.example.fraga.HubSpot.adapters.input.contactCreate.ContactResponse;
import com.example.fraga.HubSpot.domain.model.Contact;
import com.example.fraga.HubSpot.port.input.ContactUseCase;
import com.example.fraga.HubSpot.port.output.ContactRepositoryPort;
import com.example.fraga.HubSpot.port.output.CrmClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService implements ContactUseCase {

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_ERROR = "ERROR";

    private final CrmClient crmClient;
    private final ContactRepositoryPort contactRepository;

    @Override
    @Transactional
    public ContactResponse createContact(ContactRequest request) {
        Contact contact = createContactFromRequest(request);
        contact = saveContactWithStatus(contact, STATUS_PENDING);

        try {
            Contact createdInHubSpot = crmClient.create(contact, "");
            updateContactStatus(contact, STATUS_SUCCESS);
            return createSuccessResponse(createdInHubSpot);
        } catch (Exception e) {
            handleError(contact, e);
            throw e;
        }
    }

    private Contact createContactFromRequest(ContactRequest request) {
        return Contact.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Contact saveContactWithStatus(Contact contact, String status) {
        contact.setStatus(status);
        Contact savedContact = contactRepository.save(contact);
        log.info("Contato salvo com status {}: {}", status, savedContact);
        return savedContact;
    }

    private void updateContactStatus(Contact contact, String status) {
        contact.setStatus(status);
        contactRepository.save(contact);
        log.info("Status do contato atualizado para {}: {}", status, contact);
    }

    private ContactResponse createSuccessResponse(Contact contact) {
        return new ContactResponse(contact.getEmail(), "Contato criado com sucesso");
    }

    private void handleError(Contact contact, Exception e) {
        log.error("Erro ao criar contato: {}", e.getMessage());
        contact.setStatus(STATUS_ERROR);
        contact.setErrorMessage(e.getMessage());
        contactRepository.save(contact);
    }
}
