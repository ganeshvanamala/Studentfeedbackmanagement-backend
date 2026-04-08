package com.example.demo.security;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthTokenService {

    private static final long TOKEN_TTL_MS = 8L * 60 * 60 * 1000;
    private final Map<String, SessionRecord> tokenStore = new ConcurrentHashMap<>();

    public IssuedToken issueToken(User user) {
        cleanupExpiredTokens();
        String token = UUID.randomUUID().toString() + "." + UUID.randomUUID();
        long expiresAt = System.currentTimeMillis() + TOKEN_TTL_MS;
        tokenStore.put(token, new SessionRecord(user.getUsername(), user.getRole(), expiresAt));
        return new IssuedToken(token, expiresAt);
    }

    public Optional<SessionRecord> validateToken(String token) {
        if (token == null || token.isBlank()) return Optional.empty();
        SessionRecord record = tokenStore.get(token);
        if (record == null) return Optional.empty();
        if (record.expiresAt() < System.currentTimeMillis()) {
            tokenStore.remove(token);
            return Optional.empty();
        }
        return Optional.of(record);
    }

    public void revokeToken(String token) {
        if (token == null || token.isBlank()) return;
        tokenStore.remove(token);
    }

    private void cleanupExpiredTokens() {
        long now = System.currentTimeMillis();
        tokenStore.entrySet().removeIf((entry) -> entry.getValue().expiresAt() < now);
    }

    public record IssuedToken(String token, long expiresAt) {
    }

    public record SessionRecord(String username, Role role, long expiresAt) {
    }
}
