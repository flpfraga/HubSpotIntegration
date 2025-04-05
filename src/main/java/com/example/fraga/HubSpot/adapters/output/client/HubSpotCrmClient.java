package com.example.fraga.HubSpot.adapters.output.client;

import com.example.fraga.HubSpot.domain.model.Contact;
import com.example.fraga.HubSpot.port.output.CrmClient;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;

@Service
@Slf4j
public class HubSpotCrmClient implements CrmClient {

    private final WebClient webClient;

    public HubSpotCrmClient(@Qualifier("hubspotWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Contact create(Contact contact, String accessToken) {
        Map<String, Object> properties = Map.of(
                "properties", Map.of(
                        "email", contact.getEmail(),
                        "firstname", contact.getFirstName(),
                        "lastname", contact.getLastName()
                )
        );

        return webClient.post()
                .uri("/crm/v3/objects/contacts")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(properties)
                .retrieve()
                .onStatus(status -> status.value() == 429, response -> {
                    String retryAfter = response.headers().asHttpHeaders().getFirst("Retry-After");
                    log.warn("Rate limit atingido. Retry-After: {} segundos", retryAfter);
                    return Mono.error(new RuntimeException("Rate limit excedido. Tente novamente em " + retryAfter + " segundos."));
                })
                .bodyToMono(JsonNode.class)
                .map(node -> Contact.builder()
                        .email(contact.getEmail())
                        .firstName(contact.getFirstName())
                        .lastName(contact.getLastName())
                        .build())
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
                .block();
    }
}
