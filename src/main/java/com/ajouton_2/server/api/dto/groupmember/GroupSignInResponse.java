package com.ajouton_2.server.api.dto.groupmember;

import lombok.Builder;

@Builder
public record GroupSignInResponse (
        String message
) {}
