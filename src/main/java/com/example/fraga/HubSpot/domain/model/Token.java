package com.example.fraga.HubSpot.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="state")
public class Token {
    private String clientId;
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private String state;
    private String clientSecret;

    public Token(String clientId, String state, String clientSecret) {
        this.clientId = clientId;
        this.state = state;
        this.clientSecret = clientSecret;
    }
}
