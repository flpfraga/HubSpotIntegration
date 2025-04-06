package com.example.fraga.HubSpot.adapters.output.client;

import com.example.fraga.HubSpot.domain.exception.BusinessException;
import com.example.fraga.HubSpot.port.output.AuthClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    @CircuitBreaker(name = "hubspotTokenValidation", fallbackMethod = "fallbackValidateCallback")
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

    public Boolean fallbackValidateCallback(String accessToken, Throwable ex) {
        log.error("Fallback ativado para validação de token: {}", ex.getMessage());
        throw new BusinessException("Erro ao validar o token no HubSpot.", ex.getMessage());
    }

    @Override
    @CircuitBreaker(name = "hubspotTokenExchange", fallbackMethod = "fallbackExchangeCodeForToken")
    public TokenResponse exchangeCodeForToken(TokenRequest tokenRequest) {
        return webClient.post()
                .uri("/oauth/v1/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("grant_type", "authorization_code")
                        .with("client_id", tokenRequest.getClientId())
                        .with("client_secret", tokenRequest.getClientSecret())
                        .with("redirect_uri", tokenRequest.getRedirectUri())
                        .with("code", tokenRequest.getCode())
                )
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }

    public TokenResponse fallbackExchangeCodeForToken(TokenRequest tokenRequest, Throwable ex) {
        log.error("Fallback ativado para troca de código por token: {}", ex.getMessage());
        throw new BusinessException("Erro ao trocar código por token no HubSpot.", ex.getMessage());
    }

}

