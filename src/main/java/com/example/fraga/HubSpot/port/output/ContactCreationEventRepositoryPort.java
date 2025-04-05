package com.example.fraga.HubSpot.port.output;

import com.example.fraga.HubSpot.domain.model.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactCreationEventRepositoryPort extends MongoRepository<Contact, String> {
} 