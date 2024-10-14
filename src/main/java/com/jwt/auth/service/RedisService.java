package com.jwt.auth.service;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.jwt.auth.model.UserRedis;

@Service
public class RedisService {

    private final RedisTemplate<String, UserRedis> redisTemplate;

    public RedisService(RedisTemplate<String, UserRedis> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveUser(String userId, UserRedis userRedis) {
        redisTemplate.opsForValue().set(userId, userRedis);
    }

    public UserRedis getUser(String userId) {
        return redisTemplate.opsForValue().get(userId);
    }

    public void saveToken(String token, String userId) {
        UserRedis userRedis = redisTemplate.opsForValue().get(userId);
        userRedis.setToken(token);
        redisTemplate.opsForValue().set(userId, userRedis);
    }

    public void saveRefreshToken(String refreshToken, String userId) {
        UserRedis userRedis = redisTemplate.opsForValue().get(userId);
        userRedis.setRefreshToken(refreshToken);
        redisTemplate.opsForValue().set(userId, userRedis);
    }

    public String getUserIdFromToken(String token) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        if (hashOperations == null) {
            throw new IllegalStateException("HashOperations not initialized");
        }
        return hashOperations.get("TOKEN", token);
    }
}
