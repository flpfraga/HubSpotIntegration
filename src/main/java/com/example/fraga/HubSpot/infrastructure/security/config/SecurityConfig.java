package com.example.fraga.HubSpot.infrastructure.security.config;

import com.example.fraga.HubSpot.infrastructure.security.filter.HubSpotAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final HubSpotAuthenticationFilter hubSpotAuthenticationFilter;

    public SecurityConfig(HubSpotAuthenticationFilter hubSpotAuthenticationFilter) {
        this.hubSpotAuthenticationFilter = hubSpotAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            .requestMatchers("/api/v1/auth/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(hubSpotAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
} 