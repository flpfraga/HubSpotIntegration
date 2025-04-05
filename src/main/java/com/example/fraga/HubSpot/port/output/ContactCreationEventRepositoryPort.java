package com.example.fraga.HubSpot.port.output;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactCreationEventRepositoryPort extends MongoRepository<ContactCreationEvent, String> {
} 