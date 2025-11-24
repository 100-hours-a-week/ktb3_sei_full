package com.example.amumal_project.api.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


public class UserRequest {

    @Builder
    @Getter
    public static class SignupRequest {
        @NonNull
        private String email;
        @NonNull
        private String password;
        @NonNull
        private String nickname;
        @JsonProperty("profile_image")
        private String profileImageUrl;

    }

    @Builder
    @Getter
    public static class LoginRequest {
        @NonNull
        private String email;
        @NonNull
        private String password;
    }

    @Builder
    @Getter
    public static class  ProfileEditRequest {
        @NonNull
        private String nickname;
        @JsonProperty("profile_image")
        private String profileImageUrl;
    }

    @Builder
    @Getter
    public static class  PasswordResetRequest {
        @NonNull
        private String new_password;
    }
}
