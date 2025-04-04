package com.example.fraga.HubSpot.application.service;

import com.example.fraga.HubSpot.adapters.input.contact.ContactRequest;
import com.example.fraga.HubSpot.adapters.input.contact.ContactResponse;
import com.example.fraga.HubSpot.domain.model.Contact;
import com.example.fraga.HubSpot.port.input.ContactUseCase;
import com.example.fraga.HubSpot.port.output.CrmClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactService implements ContactUseCase {

    private final CrmClient crmGateway;

    @Override
    public ContactResponse createContact(ContactRequest request) {
        Contact contact = Contact.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        Contact created = crmGateway.create(contact, "");
        return new ContactResponse(created.getEmail(), "Contato criado com sucesso");
    }
}
