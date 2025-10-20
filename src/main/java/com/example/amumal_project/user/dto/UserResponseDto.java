package com.example.amumal_project.user.dto;

import com.example.amumal_project.user.User;

public class UserResponseDto {
    private Long id;
    private String nickname;
    private String profileImageUrl;

    public UserResponseDto(Long id, String nickname, String profileImageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

}
