package com.ajouton_2.server.api.dto.auth;

import lombok.Getter;

@Getter
public class SignInRequest {
    private String email;
    private String password;
}
