package com.example.amumal_project.api.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String nickname;
    @JsonProperty("profile_image")
    private String profileImageUrl;
}
