package com.example.amumal_project.api.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileImageUrl;
}
