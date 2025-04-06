package com.example.fraga.HubSpot.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Value("${hubspot.api.base-url}")
    private String baseUrl;

    @Value("${hubspot.api.timeout}")
    private Duration timeout;

    @Bean("hubspotWebClient")
    public WebClient hubspotWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .responseTimeout(timeout)
                ))
                .build();
    }
}

