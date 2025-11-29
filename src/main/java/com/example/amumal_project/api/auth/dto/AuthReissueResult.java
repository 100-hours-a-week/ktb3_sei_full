package com.example.amumal_project.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthReissueResult {
    private String accessToken;
    private String refreshToken;
}
