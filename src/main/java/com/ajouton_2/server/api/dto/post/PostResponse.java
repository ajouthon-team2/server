package com.ajouton_2.server.api.dto.post;

import lombok.Builder;

import java.util.List;

@Builder
public record PostResponse(
        Long postId,
        String title,
        String content,
        List<String> fileUrls,
        List<String> participantNames,
        String createdAt // ISO-8601 포맷 문자열로 반환 (e.g., "2024-05-24T15:22:01")
) {}