package com.ajouton_2.server.api.service;

import com.ajouton_2.server.api.dto.SignInRequest;
import com.ajouton_2.server.api.dto.TokenResponse;
import com.ajouton_2.server.common.JwtUtil;
import com.ajouton_2.server.domain.member.Member;
import com.ajouton_2.server.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse signIn(SignInRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtUtil.generateAccessToken(member);
        String refreshToken = jwtUtil.generateRefreshToken(member);

        return new TokenResponse("로그인 성공", accessToken, refreshToken);
    }
}