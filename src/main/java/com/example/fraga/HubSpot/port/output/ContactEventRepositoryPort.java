package com.example.fraga.HubSpot.port.output;

import com.example.fraga.HubSpot.domain.model.ContactEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactEventRepositoryPort extends MongoRepository<ContactEvent, String> {
} 