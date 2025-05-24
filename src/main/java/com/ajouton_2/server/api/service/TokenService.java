package com.ajouton_2.server.api.service;

import com.ajouton_2.server.api.dto.auth.TokenResponse;
import com.ajouton_2.server.common.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtil jwtUtil;

    public TokenResponse reIssue(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 refresh token입니다.");
        }

        String email = jwtUtil.getEmailFromToken(refreshToken);

        String newAccessToken = jwtUtil.generateAccessToken(email);

        return TokenResponse.of(newAccessToken, refreshToken);
    }
}
