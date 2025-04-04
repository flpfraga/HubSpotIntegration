package com.example.fraga.HubSpot.adapters.input.contact;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContactResponse {
    private String hubspotId;
    private String message;
}
