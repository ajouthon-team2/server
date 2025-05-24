package com.ajouton_2.server.api.controller;

import com.ajouton_2.server.api.dto.auth.TokenResponse;
import com.ajouton_2.server.api.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/re-issue")
    public ResponseEntity<TokenResponse> reIssueToken(@RequestHeader("Authorization") String authorizationHeader) {
        String refreshToken = extractToken(authorizationHeader);
        TokenResponse tokenResponse = tokenService.reIssue(refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

    private String extractToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new IllegalArgumentException("Authorization 헤더 형식이 잘못되었습니다.");
    }
}
