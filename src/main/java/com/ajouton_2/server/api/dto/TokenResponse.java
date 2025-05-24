package com.ajouton_2.server.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
    private String message;
    private String accessToken;
    private String refreshToken;
}
