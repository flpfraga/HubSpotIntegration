package com.example.fraga.HubSpot.application.service;

import com.example.fraga.HubSpot.adapters.input.auth.AuthRequest;
import com.example.fraga.HubSpot.adapters.input.auth.AuthResponse;
import com.example.fraga.HubSpot.domain.model.Token;
import com.example.fraga.HubSpot.adapters.output.client.TokenRequest;
import com.example.fraga.HubSpot.adapters.output.client.TokenResponse;
import com.example.fraga.HubSpot.port.input.AuthHubSpotUseCase;
import com.example.fraga.HubSpot.port.output.AuthClient;
import com.example.fraga.HubSpot.port.output.CryptoService;
import com.example.fraga.HubSpot.port.output.TokenStorage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AuthHubSpotServiceImpl implements AuthHubSpotUseCase {

    private final AuthClient authClient;
    private final TokenStorage tokenStorage;
    private final CryptoService cryptoService;
    private final ModelMapper mapper;

    @Value("${hubspot.api.redirect-uri}")
    private String redirectUri;

    @Value("${hubspot.api.authorization-url}")
    private String authorizationUrl;

    public AuthHubSpotServiceImpl(AuthClient authClient,
                                  TokenStorage tokenStorage,
                                  CryptoService cryptoService,
                                  ModelMapper mapper) {
        this.authClient = authClient;
        this.tokenStorage = tokenStorage;
        this.cryptoService = cryptoService;
        this.mapper = mapper;
    }

    @Override
    public AuthResponse generateAuthorizationUrl(AuthRequest authRequest) {
        String encryptClientSecret = cryptoService.encrypt(authRequest.getClientSecret());
        tokenStorage.saveTokenData(new Token(authRequest.getClientId(), authRequest.getState(), encryptClientSecret));
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
        var tokenReponse = generateToken(state, code);
        validateTokenInHubSpotApi(tokenReponse.getAccess_token());
        return new AuthResponse(
                null,
                state,
                tokenReponse.getAccess_token(),
                tokenReponse.getRefresh_token(),
                Long.getLong(tokenReponse.getExpires_in()));
    }

    private void validateTokenInHubSpotApi(String accessToken){
        if (Boolean.FALSE.equals(authClient.validateCallback(accessToken))){
            throw new RuntimeException("Ocorreu um erro na validação do token.");
        }
    }

    private TokenResponse generateToken(String state, String code) {
        var token = tokenStorage.findTokenById(state)
                .orElseThrow(() -> new RuntimeException("erro ao buscar no redis"));
        token.setClientSecret(cryptoService.decrypt(token.getClientSecret()));
        TokenRequest tokenRequest = mapper.map(token, TokenRequest.class);
        tokenRequest.setCode(code);
        tokenRequest.setRedirectUri(redirectUri);
        return authClient.exchangeCodeForToken(tokenRequest);
    }

    @Override
    public void validateToken(String accessToken) {
        validateTokenInHubSpotApi(accessToken);
    }
}