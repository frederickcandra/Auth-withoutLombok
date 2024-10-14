package com.jwt.auth.service;

import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.jwt.auth.model.Role;
import com.jwt.auth.model.User;
import com.jwt.auth.model.UserRedis;
import com.jwt.auth.request.*;
import com.jwt.auth.response.JwtResponse;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisService redisService;

    public AuthService(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, RedisService redisService) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.redisService = redisService;
    }

    public UserRedis register(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);

        UserRedis userRedis = new UserRedis();
        userRedis.setUsername(user.getUsername());
        userRedis.setPassword(user.getPassword());
        userRedis.setRole(user.getRole());

        redisService.saveUser(user.getUsername(), userRedis);

        return userRedis;
    }

    public JwtResponse login(LoginRequest loginRequest) {
        UserRedis userRedis = redisService.getUser(loginRequest.getUsername());
        if (userRedis == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        User user = new User();
        user.setUsername(userRedis.getUsername());
        user.setPassword(userRedis.getPassword());
        user.setRole(userRedis.getRole());

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        var token = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        redisService.saveToken(token, user.getUsername());
        redisService.saveRefreshToken(refreshToken, user.getUsername());

        userRedis.setRefreshToken(refreshToken);
        redisService.saveUser(userRedis.getUsername(), userRedis);

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setToken(token);
        jwtResponse.setRefreshToken(refreshToken);
        jwtResponse.setRole(user.getRole());
        jwtResponse.setMessage("PROFILE INFORMATION");

        return jwtResponse;
    }

    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        UserRedis userRedis = redisService.getUser(refreshTokenRequest.getUsername());
        String refreshToken = userRedis.getRefreshToken();

        if (userRedis == null || refreshToken == null || refreshToken.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        String username = jwtService.extractUsername(refreshToken);

        User user = new User();
        user.setUsername(userRedis.getUsername());
        user.setPassword(userRedis.getPassword());
        user.setRole(userRedis.getRole());

        if (jwtService.isTokenValid(refreshToken, user)) {
            String newAccessToken = jwtService.generateToken(user);
            String newRefreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

            redisService.saveToken(newAccessToken, user.getUsername());
            redisService.saveRefreshToken(newRefreshToken, user.getUsername());

            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setToken(newAccessToken);
            jwtResponse.setRefreshToken(newRefreshToken);
            jwtResponse.setRole(user.getRole());
            jwtResponse.setMessage("Refresh Token Success");

            return jwtResponse;
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
    }

    public JwtResponse changeRole(ChangeRoleRequest changeRoleRequest) {
        UserRedis userRedis = redisService.getUser(changeRoleRequest.getUsername());
        if (userRedis == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        userRedis.setRole(Role.ADMIN);
        redisService.saveUser(userRedis.getUsername(), userRedis);

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setMessage("Success Change Role");
        jwtResponse.setRole(userRedis.getRole());
        jwtResponse.setToken(userRedis.getToken());

        return jwtResponse;
    }

    public boolean validate(TokenRequest tokenRequest) {
        String username = jwtService.extractUsername(tokenRequest.getToken());
        UserRedis userRedis = redisService.getUser(username);

        if (userRedis == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        User user = new User();
        user.setUsername(userRedis.getUsername());
        user.setPassword(userRedis.getPassword());
        user.setRole(userRedis.getRole());

        if (jwtService.isTokenValid(tokenRequest.getToken(), user)) {
            return true;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
    }
}
