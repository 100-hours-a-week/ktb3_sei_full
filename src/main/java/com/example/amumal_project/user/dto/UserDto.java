package com.example.amumal_project.user.dto;

public class UserDto {
    private Long id;
    private String nickname;
    private String profileImageUrl;

    public UserDto(Long id, String nickname, String profileImageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }



}
