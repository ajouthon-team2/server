package com.ajouton_2.server.common;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private Key key;
    private final long ACCESS_EXPIRATION = 1000L * 60 * 30; // 30분
    private final long REFRESH_EXPIRATION = 1000L * 60 * 60 * 24 * 14; // 2주
    private final String SECRET = "ajouton2-secret-key-which-is-at-least-256-bits-long";

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateAccessToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("auth", "ROLE_USER")
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("auth", "ROLE_USER")
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String getEmailFromLogin(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization 헤더가 유효하지 않습니다.");
        }

        // 1. "Bearer " 제거 → JWT만 추출
        String token = authorizationHeader.substring(7);
        log.info("JWT 토큰에서 추출된 사용자 이메일: {}", token);
        // 2. JWT 파싱 및 검증
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 3. subject(email) 반환
            String email = claims.getSubject();
            log.info("JWT 토큰에서 추출된 사용자 이메일: {}", email);
            return email;

        } catch (JwtException e) {
            log.error("JWT 토큰 파싱 실패: {}", e.getMessage());
            throw new IllegalArgumentException("유효하지 않은 JWT 토큰입니다.");
        }
    }
}
