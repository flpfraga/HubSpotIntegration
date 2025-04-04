package com.example.fraga.HubSpot.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private String encryptedToken;
    private LocalDateTime expirationDate;

    public Token(String clientId, String state, String clientSecret) {
        this.clientId = clientId;
        this.state = state;
        this.clientSecret = clientSecret;
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    public boolean isExpired() {
        return expirationDate != null && LocalDateTime.now().isAfter(expirationDate);
    }
}
