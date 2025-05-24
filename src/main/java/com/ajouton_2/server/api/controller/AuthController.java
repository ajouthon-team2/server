package com.ajouton_2.server.api.controller;

import com.ajouton_2.server.api.dto.auth.SignInRequest;
import com.ajouton_2.server.api.dto.auth.TokenResponse;
import com.ajouton_2.server.api.service.AuthService;
import com.ajouton_2.server.api.service.MemberService;
import com.ajouton_2.server.api.dto.auth.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<TokenResponse> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authService.signIn(request));
    }


    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        try {
            memberService.signUp(request);
            return ResponseEntity.ok().body(Map.of("message", "회원가입 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}