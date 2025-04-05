package com.example.fraga.HubSpot.port.input;

import com.example.fraga.HubSpot.adapters.input.contactCreate.ContactRequest;
import com.example.fraga.HubSpot.adapters.input.contactCreate.ContactResponse;

public interface ContactUseCase {
    ContactResponse createContact(ContactRequest contactRequest, String accessToken);
}
