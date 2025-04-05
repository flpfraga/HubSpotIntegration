package com.example.fraga.HubSpot.infrastructure.security.filter;

import com.example.fraga.HubSpot.domain.exception.BusinessException;
import com.example.fraga.HubSpot.domain.exception.ErrorCode;
import com.example.fraga.HubSpot.domain.model.Token;
import com.example.fraga.HubSpot.port.output.TokenStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubSpotAuthenticationFilter extends OncePerRequestFilter {

    private final TokenStorage tokenStorage;
    private final ObjectMapper objectMapper;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String STATE_HEADER = "X-Hubspot-State";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        String contextPath = request.getContextPath();
        String fullPath = contextPath + path;
        
        log.debug("m=shouldNotFilter path={} contextPath={} fullPath={}", path, contextPath, fullPath);
        
        // Ignora endpoints públicos (alinhado com SecurityConfig)
        return path.startsWith("/api/v1/auth") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // Se chegou aqui, é um endpoint que requer autenticação
            String token = extractToken(request);
            String state = extractState(request);
            
            if (token == null || state == null) {
                log.warn("m=doFilterInternal status=unauthorized reason=missing_headers path={} contextPath={}", 
                    request.getServletPath(), request.getContextPath());
                sendErrorResponse(response, new BusinessException(
                    ErrorCode.UNAUTHORIZED.getCode(),
                    "Headers de autenticação ausentes"
                ));
                return;
            }
            
            validateTokenAndState(token, state);

            Authentication auth = new UsernamePasswordAuthenticationToken(state, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            log.error("m=doFilterInternal status=error exception={} message={} path={} contextPath={}", 
                e.getClass().getSimpleName(), e.getMessage(), request.getServletPath(), request.getContextPath());
            sendErrorResponse(response, e);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, BusinessException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", e.getCode());
        errorResponse.put("message", e.getMessage());
        
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

    private void validateTokenAndState(String token, String state) {
        Token storedToken = tokenStorage.findTokenById(state)
            .orElseThrow(() -> new BusinessException(
                ErrorCode.UNAUTHORIZED.getCode(),
                "Token não encontrado"
            ));

        if (!token.equals(storedToken.getAccessToken())) {
            throw new BusinessException(
                ErrorCode.UNAUTHORIZED.getCode(),
                "Token inválido"
            );
        }

        if (storedToken.isExpired()) {
            throw new BusinessException(
                ErrorCode.UNAUTHORIZED.getCode(),
                "Token expirado"
            );
        }
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private String extractState(HttpServletRequest request) {
        return request.getHeader(STATE_HEADER);
    }
} 