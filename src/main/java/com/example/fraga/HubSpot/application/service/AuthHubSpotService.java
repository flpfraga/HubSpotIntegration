package com.example.fraga.HubSpot.application.service;

import com.example.fraga.HubSpot.adapters.input.auth.AuthRequest;
import com.example.fraga.HubSpot.adapters.input.auth.AuthResponse;
import com.example.fraga.HubSpot.domain.exception.BusinessException;
import com.example.fraga.HubSpot.domain.exception.ErrorCode;
import com.example.fraga.HubSpot.domain.model.Token;
import com.example.fraga.HubSpot.adapters.output.client.TokenRequest;
import com.example.fraga.HubSpot.adapters.output.client.TokenResponse;
import com.example.fraga.HubSpot.port.input.AuthHubSpotUseCase;
import com.example.fraga.HubSpot.port.output.AuthClient;
import com.example.fraga.HubSpot.port.output.CryptoService;
import com.example.fraga.HubSpot.port.output.TokenStorage;
import com.example.fraga.HubSpot.shared.ConverterUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class AuthHubSpotService implements AuthHubSpotUseCase {

    private final AuthClient authClient;
    private final TokenStorage tokenStorage;
    private final CryptoService cryptoService;
    private final ModelMapper mapper;

    @Value("${hubspot.api.redirect-uri}")
    private String redirectUri;

    @Value("${hubspot.api.authorization-url}")
    private String authorizationUrl;

    public AuthHubSpotService(AuthClient authClient,
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

        log.info("m=generateAuthorizationUrl status=success");
        return AuthResponse.builder()
                .authorizationUrl(UriComponentsBuilder.fromHttpUrl(authorizationUrl)
                        .queryParam("client_id", authRequest.getClientId())
                        .queryParam("redirect_uri", redirectUri)
                        .queryParam("scope",
                                URLEncoder.encode(authRequest.getScope(), StandardCharsets.UTF_8)
                                        .replace("+", "%20")
                        )
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

        updateTokenAfterGenerate(tokenReponse, state);
        log.info("m=handleCallback status=success");
        return new AuthResponse(
                null,
                state,
                tokenReponse.getAccess_token(),
                tokenReponse.getRefresh_token(),
                ConverterUtils.converterStringToLong(tokenReponse.getExpires_in()));
    }

    private void updateTokenAfterGenerate(TokenResponse tokenResponse, String state) {
        tokenStorage.updateTokenAfterGenerate(tokenResponse, state);
    }

    private void validateTokenInHubSpotApi(String accessToken) {
        if (Boolean.FALSE.equals(authClient.validadeGeneratedToken(accessToken))) {
            logError();
            throw new BusinessException(ErrorCode.UNAUTHORIZED.getCode(), "Erro na validação do token.");
        }
    }

    private TokenResponse generateToken(String state, String code) {
        var token = tokenStorage.findTokenByState(state)
                .orElseThrow(() -> {
                    logError();
                    return new BusinessException(ErrorCode.UNAUTHORIZED.getCode(),
                            "State não encontrado.");
                });
        token.setClientSecret(cryptoService.decrypt(token.getClientSecret()));
        TokenRequest tokenRequest = mapper.map(token, TokenRequest.class);
        tokenRequest.setCode(code);
        tokenRequest.setRedirectUri(redirectUri);
        return authClient.exchangeCodeForToken(tokenRequest);
    }

    private static void logError() {
        log.error("m=handleCallback status=error message={}", "Erro ao buscar no Redis");
    }

}