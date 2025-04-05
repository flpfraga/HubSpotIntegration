package com.example.fraga.HubSpot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "hubspot.webhook")
public class WebhookConfig {
    private String secret;
    private List<String> subscriptionTypes;
    private String secretToken;
    private String event;
} 