package com.example.fraga.HubSpot.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "contact_events")
public class ContactEvent {
    @Id
    private String id;
    private String subscriptionId;
    private String portalId;
    private String appId;
    private Long occurredAt;
    private String subscriptionType;
    private String attemptNumber;
    private String objectId;
    private String changeFlag;
    private String changeSource;
    private String eventId;
    private String propertyName;
    private String propertyValue;
} 