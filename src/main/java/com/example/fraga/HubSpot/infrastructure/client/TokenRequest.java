package com.example.fraga.HubSpot.infrastructure.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest {
    private String clientId;
    private String clientSecret;
    private String code;
    private String redirectUri;
}
