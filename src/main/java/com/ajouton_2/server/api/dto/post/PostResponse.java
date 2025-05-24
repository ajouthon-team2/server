package com.ajouton_2.server.api.dto.post;

import lombok.Builder;

import java.util.List;

@Builder
public record PostResponse(
        Long postId,
        String title,
        String content,
        List<String> fileUrls,
        List<Long> participantIds
) {}

