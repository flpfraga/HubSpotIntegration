package com.example.fraga.HubSpot.application.service;

import com.example.fraga.HubSpot.adapters.input.auth.AuthRequest;
import com.example.fraga.HubSpot.adapters.input.auth.AuthResponse;
import com.example.fraga.HubSpot.domain.exception.InvalidTokenException;
import com.example.fraga.HubSpot.domain.model.Token;
import com.example.fraga.HubSpot.port.input.AuthHubSpotUseCase;
import com.example.fraga.HubSpot.port.input.CryptoService;
import com.example.fraga.HubSpot.port.output.AuthClient;
import com.example.fraga.HubSpot.port.output.TokenStorage;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AuthHubSpotServiceImpl implements AuthHubSpotUseCase {

    private final AuthClient authClient;
    private final TokenStorage tokenStorage;
    private final CryptoService cryptoService;

    @Value("${hubspot.api.redirect-uri}")
    private String redirectUri;

    @Value("${hubspot.api.authorization-url}")
    private String authorizationUrl;

    public AuthHubSpotServiceImpl(AuthClient authClient, TokenStorage tokenStorage, CryptoService cryptoService) {
        this.authClient = authClient;
        this.tokenStorage = tokenStorage;
        this.cryptoService = cryptoService;
    }

    @Override
    public AuthResponse generateAuthorizationUrl(AuthRequest authRequest) {
        String encryptClientSecret  = cryptoService.encrypt(authRequest.getClientSecret());
        tokenStorage.saveTokenData(new Token(authRequest.getClientId(),authRequest.getState(), encryptClientSecret));
        return AuthResponse.builder()
                .authorizationUrl(UriComponentsBuilder.fromHttpUrl(authorizationUrl)
                        .queryParam("client_id", authRequest.getClientId())
                        .queryParam("redirect_uri", redirectUri)
                        .queryParam("scope", authRequest.getScope())
                        .queryParam("state", authRequest.getState())
                        .queryParam("response_type", "code")
                        .build()
                        .toUriString())
                .state(authRequest.getState())
                .build();
    }

    @Override
    public AuthResponse handleCallback(String code, String state) {
        generateToken()
        validateToken(code, state);
        return new AuthResponse(null, state, code);
    }

    private void generateToken(String state, String code){
        var token = tokenStorage.findTokenById(state)
                .orElseThrow(() -> new RuntimeException("erro ao buscar no redis"));

    }

    @Override
    public void validateToken(String code, String state) {
        if (StringUtils.isBlank(code) || StringUtils.isBlank(state)) {
            throw new RuntimeException("token ou code invalido");
        }
        if (tokenIsValid(code)) {
            tokenStorage.updateAccessToken(state, code);
        }
        throw new RuntimeException("token invalido");
    }

    private Token findTokenByState(String state) {
        return tokenStorage.findTokenById(state)
                .orElseThrow(
                        RuntimeException::new
                );
    }

    private Boolean tokenIsValid(String code) {
        return authClient.validateCallback(code);
    }

} 