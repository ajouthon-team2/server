package com.ajouton_2.server.api.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String email;
    private String password;
    private String name;
    private String studentId;
    private String phoneNumber;
    private String department;
}