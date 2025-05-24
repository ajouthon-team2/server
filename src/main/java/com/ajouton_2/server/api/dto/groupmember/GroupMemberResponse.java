package com.ajouton_2.server.api.dto.groupmember;

public record GroupMemberResponse(
        Long memberId,
        String name,
        String email,
        String role // LEADER 또는 MEMBER
) {}