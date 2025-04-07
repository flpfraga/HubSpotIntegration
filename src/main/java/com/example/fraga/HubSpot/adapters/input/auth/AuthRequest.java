package com.example.fraga.HubSpot.adapters.input.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class AuthRequest {
    @NotBlank(message = "O clientId é obrigatório")
    private final String clientId;

    @NotBlank(message = "O scope é obrigatório")
    private final String scope;

    @NotBlank(message = "O state é obrigatório")
    private final String state;

    @NotBlank(message = "O clientSecret é obrigatório")
    private final String clientSecret;
} 