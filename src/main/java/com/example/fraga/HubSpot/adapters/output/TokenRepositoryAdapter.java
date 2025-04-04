package com.example.fraga.HubSpot.adapters.output;

import com.example.fraga.HubSpot.domain.model.Token;
import com.example.fraga.HubSpot.port.output.TokenRepositoryPort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TokenRepositoryAdapter implements TokenRepositoryPort {

    private final RedisTemplate<String, Token> redisTemplate;
    private static final String TOKEN_KEY_PREFIX = "token:";

    public TokenRepositoryAdapter(RedisTemplate<String, Token> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Token findByEncryptedToken(String encryptedToken) {
        return redisTemplate.opsForValue().get(TOKEN_KEY_PREFIX + encryptedToken);
    }

    @Override
    public void save(Token token) {
        redisTemplate.opsForValue().set(TOKEN_KEY_PREFIX + token.getEncryptedToken(), token);
    }
} 