package com.example.fraga.HubSpot.adapters.input.contactWebhook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactWebhookResponse {
    private String message;
}
