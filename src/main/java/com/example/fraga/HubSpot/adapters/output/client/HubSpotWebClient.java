package com.example.fraga.HubSpot.adapters.output.client;

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

    @Override
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

    private Long convertToLongOrNull(String data){
        try{
            return Long.getLong(data);
        }
        catch (Exception e){
            log.info("Erro ao converter o valor de expiração.");
        }
        return null;
    }
}

