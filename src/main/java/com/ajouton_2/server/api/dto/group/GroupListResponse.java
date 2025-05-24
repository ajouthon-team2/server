package com.ajouton_2.server.api.dto.group;

public record GroupListResponse(
        Long groupId,
        String groupName,
        String category,
        String role
) {}
