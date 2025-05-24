package com.ajouton_2.server.api.controller;

import com.ajouton_2.server.api.dto.SignInRequest;
import com.ajouton_2.server.api.dto.TokenResponse;
import com.ajouton_2.server.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signIn")
    public ResponseEntity<TokenResponse> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authService.signIn(request));
    }


    private final MemberService memberService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        try {
            memberService.signUp(request);
            return ResponseEntity.ok().body(Map.of("message", "회원가입 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
