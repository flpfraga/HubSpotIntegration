package com.example.fraga.HubSpot.infrastructure.security.filter;

import com.example.fraga.HubSpot.adapters.output.repository.RedisTokenRepository;
import com.example.fraga.HubSpot.domain.model.Token;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HubSpotAuthenticationFilterTest {

    @Mock
    private RedisTokenRepository redisTokenRepository;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private HubSpotAuthenticationFilter filter;

    private Token token;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        
        token = Token.builder()
                .accessToken("test-token")
                .refreshToken("test-refresh-token")
                .expiresIn(3600L)
                .build();

        userDetails = new User("test@example.com", "", Collections.emptyList());
//        request.setAttribute("X-Hubspot-State", "state");
    }

    @Test
    void doFilterInternal_shouldSetAuthentication_whenTokenIsValid() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer test-token");
        when(request.getHeader("X-Hubspot-State")).thenReturn("State");
        when(redisTokenRepository.findTokenByState("test-token")).thenReturn(Optional.of(token));
        when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(userDetails, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldNotSetAuthentication_whenTokenIsInvalid() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer test-token");
        when(request.getHeader("X-Hubspot-State")).thenReturn("State");
        when(redisTokenRepository.findTokenByState("test-token"));

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldNotSetAuthentication_whenTokenIsExpired() throws ServletException, IOException {
        // Arrange
        Token expiredToken = Token.builder()
                .accessToken("expired-token")
                .refreshToken("test-refresh-token")
                .expiresIn(0L)
                .build();

        when(request.getHeader("Authorization")).thenReturn("Bearer expired-token");
        when(redisTokenRepository.findTokenByState("test-token")).thenReturn(Optional.of(expiredToken));

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldNotSetAuthentication_whenRedisFails() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer test-token");
        when(redisTokenRepository.findTokenByState("test-token")).thenThrow(new RuntimeException("Redis error"));

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        
        verify(filterChain, times(1)).doFilter(request, response);
    }
} 