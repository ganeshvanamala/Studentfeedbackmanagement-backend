package com.example.demo.security;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;
import java.util.Date;

@Service
public class AuthTokenService {

    private final long tokenTtlMs;
    private final SecretKey signingKey;

    public AuthTokenService(
            @Value("${app.jwt.secret:8xK2mP9sQ4vL7nR1zT6yW3aB5cD8eF0hJ2kL9pQ7rS4t}") String jwtSecret,
            @Value("${app.jwt.ttl-ms:28800000}") long tokenTtlMs
    ) {
        this.tokenTtlMs = tokenTtlMs;
        this.signingKey = buildSigningKey(jwtSecret);
    }

    public IssuedToken issueToken(User user) {
        long now = System.currentTimeMillis();
        long expiresAt = now + tokenTtlMs;
        String token = Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole().name())
                .claim("userId", user.getId())
                .issuedAt(new Date(now))
                .expiration(new Date(expiresAt))
                .signWith(signingKey)
                .compact();
        return new IssuedToken(token, expiresAt);
    }

    public Optional<SessionRecord> validateToken(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String username = claims.getSubject();
            String roleValue = claims.get("role", String.class);
            Date expiration = claims.getExpiration();

            if (username == null || roleValue == null || expiration == null) {
                return Optional.empty();
            }

            Role role = Role.valueOf(roleValue);
            return Optional.of(new SessionRecord(username, role, expiration.getTime()));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public void revokeToken(String token) {
        // JWTs are stateless. Logging out is handled client-side by removing the token.
    }

    private SecretKey buildSigningKey(String jwtSecret) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(jwtSecret.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(digest, "HmacSHA256");
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to initialize JWT signing key", ex);
        }
    }

    public record IssuedToken(String token, long expiresAt) {
    }

    public record SessionRecord(String username, Role role, long expiresAt) {
    }
}
