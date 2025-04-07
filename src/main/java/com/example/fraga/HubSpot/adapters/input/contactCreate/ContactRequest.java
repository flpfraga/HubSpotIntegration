package com.example.fraga.HubSpot.adapters.input.contactCreate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactRequest {
    private String email;
    private String firstName;
    private String lastName;
}
