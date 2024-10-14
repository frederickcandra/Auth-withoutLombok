package com.jwt.auth.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jwt.auth.model.User;
import com.jwt.auth.model.UserRedis;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final RedisTemplate<String, UserRedis> redisTemplate;

    public UserService(RedisTemplate<String, UserRedis> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveUser(User user) {
        // Convert User to UserRedis
        UserRedis userRedis = convertToUserRedis(user);
        redisTemplate.opsForValue().set(user.getUsername(), userRedis);
    }

    public User findUserByUsername(String username) {
        // Fetch UserRedis from Redis
        UserRedis userRedis = Optional.ofNullable(redisTemplate.opsForValue().get(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Convert UserRedis to User
        return convertToUser(userRedis);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByUsername(username);
    }

    private UserRedis convertToUserRedis(User user) {
        UserRedis userRedis = new UserRedis();
        userRedis.setUsername(user.getUsername());
        userRedis.setPassword(user.getPassword()); // Pertimbangkan untuk mengenkripsi password
        userRedis.setRole(user.getRole());
        return userRedis;
    }

    private User convertToUser(UserRedis userRedis) {
        User user = new User();
        user.setUsername(userRedis.getUsername());
        user.setPassword(userRedis.getPassword());
        user.setRole(userRedis.getRole());
        return user;
    }
}
