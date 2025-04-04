package com.example.fraga.HubSpot.port.input;

import com.example.fraga.HubSpot.adapters.input.contact.ContactRequest;
import com.example.fraga.HubSpot.adapters.input.contact.ContactResponse;

public interface ContactUseCase {
    ContactResponse createContact(ContactRequest request);
}
