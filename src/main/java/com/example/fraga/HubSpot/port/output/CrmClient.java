package com.example.fraga.HubSpot.port.output;

import com.example.fraga.HubSpot.domain.model.Contact;

public interface CrmClient {

    Contact create(Contact contact, String accessToken);
}
