package com.example.fraga.HubSpot.adapters.output.repository;

import com.example.fraga.HubSpot.adapters.output.client.TokenResponse;
import com.example.fraga.HubSpot.domain.exception.ErrorCode;
import com.example.fraga.HubSpot.domain.exception.InfrastructureException;
import com.example.fraga.HubSpot.domain.model.Token;
import com.example.fraga.HubSpot.port.output.TokenStorage;
import com.example.fraga.HubSpot.shared.ConverterUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisTokenRepository implements TokenStorage {

    private final RedisTemplate<String, Token> redisTemplate;

    @Override
    public void saveTokenData(Token token) {
        redisTemplate.opsForValue().set("token:" + token.getState(), token, Duration.ofHours(6));
    }

    @Override
    public Optional<Token> findTokenByState(String state) {
        var token = redisTemplate.opsForValue().get("token:" + state);
        return Optional.ofNullable(token);
    }

    @Override
    public void updateTokenAfterGenerate(TokenResponse tokenResponse, String state) {
        var token = redisTemplate.opsForValue().get("token:" + state);
        if (ObjectUtils.isEmpty(token)) {
            throw new InfrastructureException(ErrorCode.DATABASE_ERROR.getCode(), "Erro ao atualizar o token");
        }
        Long expiresIn = ConverterUtils.converterStringToLong(tokenResponse.getExpires_in());
        token.setAccessToken(tokenResponse.getAccess_token());
        token.setRefreshToken(tokenResponse.getRefresh_token());
        token.setTokenType(tokenResponse.getToken_type());
        token.setExpiresIn(expiresIn);
        token.setDataExpires(LocalDateTime.now().plus(Duration.ofSeconds(expiresIn)).toString());
        redisTemplate.opsForValue().set("token:" + token.getState(), token, Duration.ofHours(6));
    }
}
