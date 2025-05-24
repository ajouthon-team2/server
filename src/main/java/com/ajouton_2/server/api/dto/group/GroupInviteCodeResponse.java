package com.ajouton_2.server.api.dto.group;

import lombok.Builder;

@Builder
public record GroupInviteCodeResponse(
    String inviteCode
) {}
