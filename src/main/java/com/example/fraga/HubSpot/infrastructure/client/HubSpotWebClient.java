package com.example.fraga.HubSpot.infrastructure.client;

import com.example.fraga.HubSpot.domain.model.Token;
import com.example.fraga.HubSpot.port.output.AuthClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class HubSpotWebClient implements AuthClient {

    @Value("${hubspot.token-validation.uri}")
    private String validationUri;

    private final WebClient webClient;

    public HubSpotWebClient(@Qualifier("hubspotWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Boolean validateCallback(String accessToken) {
        return webClient.get()
                .uri(validationUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> {
                            log.warn("Token inválido: {}", clientResponse.statusCode());
                            return Mono.empty(); // indica token inválido
                        })
                .bodyToMono(String.class)
                .map(response -> true)
                .defaultIfEmpty(false)
                .block();
    }

    public Mono<Token> exchangeCodeForToken(Token token) {
        return webClient.post()
                .uri("/oauth/v1/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("grant_type", "authorization_code")
                        .with("client_id", token.getClientId())
                        .with("client_secret", token.)
                        .with("redirect_uri", redirectUri)
                        .with("code", code)
                )
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .map(res -> new Token(
                        clientId,
                        res.getAccess_token(),
                        res.getRefresh_token(),
                        null // ou algum estado, se você quiser manter
                ));
    }
}

