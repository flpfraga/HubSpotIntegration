package com.example.fraga.HubSpot.adapters.input.auth;

import lombok.*;

@Builder
@AllArgsConstructor
@Data
public class AuthResponse {
    private final String authorizationUrl;
    private final String state;
    private final String accessToken;
    private final String refreshToken;
    private final Long expiresIn;
}