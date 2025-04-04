package com.example.fraga.HubSpot.application.service;

import com.example.fraga.HubSpot.domain.model.Token;
import com.example.fraga.HubSpot.port.output.TokenStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenStorageService implements TokenStorage {

    private final RedisTemplate<String, Token> redisTemplate;

    @Override
    public void saveTokenData(Token token) {
        redisTemplate.opsForValue().set("token:" + token.getState(), token, Duration.ofHours(2));
    }

    @Override
    public Optional<Token> findTokenById(String state){
        var token = redisTemplate.opsForValue().get("token:" +state);
        return Optional.ofNullable(token);
    }

    @Override
    public Token updateAccessToken(String state, String accessToken){
        var token = redisTemplate.opsForValue().get("token:" +state);
        if (ObjectUtils.isEmpty(token)){
            throw new RuntimeException("Erro ao atualizar o token");
        }
        token.setAccessToken(accessToken);
        redisTemplate.opsForValue().set("token:" + token.getState(), token, Duration.ofHours(2));
        return token;
    }
}
