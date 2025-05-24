//package com.ajouton_2.server.common;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//
//@Component
//public class JwtUtil {
//
//    private final SecretKey secretKey;
//    private final long accessTokenExpiration; // 30분
//    private final long refreshTokenExpiration; // 2시간
//
//    public JwtUtil(@Value("${jwt.secret}") String secret,
//                   @Value("${jwt.access.expirationMs}") long accessTokenExpiration,
//                   @Value("${jwt.refresh.expirationMs}") long refreshTokenExpiration) {
//        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//        this.accessTokenExpiration = accessTokenExpiration;
//        this.refreshTokenExpiration = refreshTokenExpiration;
//    }
//
//    public String createJwt(Long memberId) {
//
//        long expiredMs = 0L;
//        if (category.equals("access")) {
//            expiredMs = accessTokenExpiration;
//        } else if (category.equals("refresh")) {
//            expiredMs = refreshTokenExpiration;
//        }
//
//        return Jwts.builder()
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + expiredMs))
//                .signWith(secretKey)
//                .compact();
//    }
//
//    public boolean isTokenExpired(String token) {
//        try {
//            Jwts
//                    .parser()
//                    .verifyWith(secretKey)
//                    .build()
//                    .parseSignedClaims(token);
//            // 예외 없이 완전히 파싱되었으면 만료되지 않음
//            return false;
//        } catch (ExpiredJwtException e) {
//            // 토큰의 exp 클레임이 현재 시각보다 이전인 경우
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            // 서명 불일치, 형식 오류 등 다른 검증 실패는
//            // '만료' 여부와 별개이므로 false로 처리
//            return false;
//        }
//    }
//
//    public boolean isTokenValid(String token) {
//        try {
//            Jwts
//                    .parser()
//                    .verifyWith(secretKey)
//                    .build()
//                    .parseSignedClaims(token);
//            // 파싱에 예외가 없었다면 서명, 만료, 형식 모두 통과
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            // 만료, 서명불일치, 잘못된 형식 등 모두 여기서 잡힘
//            return false;
//        }
//    }
//
//}
