package com.ajouton_2.server.api.service;

import com.ajouton_2.server.common.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.ajouton_2.server.domain.member.MemberJpaRepository;
import com.ajouton_2.server.api.dto.auth.SignUpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ajouton_2.server.domain.member.Member;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .studentId(request.getStudentId())
                .phoneNumber(request.getPhoneNumber())
                .department(request.getDepartment())
                .build();

        memberRepository.save(member);
    }

    public Member getLoginedMember() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Member Not Found"));
    }
}
