package com.example.fraga.HubSpot.adapters.input.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthRequest {
    private final String clientId;
    private final String scope;
    private final String state;
    private final String clientSecret;
} 