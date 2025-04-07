package com.example.fraga.HubSpot.domain.model;

import com.example.fraga.HubSpot.shared.ConverterUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String tokenType;
    private String dataExpires;

    public Token(String clientId, String state, String clientSecret) {
        this.clientId = clientId;
        this.state = state;
        this.clientSecret = clientSecret;
    }

    @JsonIgnore
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(LocalDateTime.parse(dataExpires));
    }
}
