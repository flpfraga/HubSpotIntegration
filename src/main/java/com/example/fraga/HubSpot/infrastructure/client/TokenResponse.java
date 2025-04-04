package com.example.fraga.HubSpot.infrastructure.client;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenResponse {
    private String access_token;
    private String refresh_token;
    private String expires_in;
    private String token_type;
}
