package com.example.fraga.HubSpot.infrastructure.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebhookSecurityConfig {

    @Bean
    public SecurityFilterChain webhookSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/v1/webhook/**")
            .csrf().disable()
            .authorizeHttpRequests()
            .anyRequest().permitAll();
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/api/v1/webhook/**");
    }
} 