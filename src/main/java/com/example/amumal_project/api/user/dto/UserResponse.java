package com.example.amumal_project.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class UserResponse {
    @Builder
    @Getter
    public static class GetUsersResponse {
        private List<UserDto> users;
    }

    @Getter
    @AllArgsConstructor
    public static class LoginResponse {
        private String message;
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @AllArgsConstructor
    public static class ReissueResponse {
        private String message;
        private String accessToken;
        private String refreshToken;
    }



}
