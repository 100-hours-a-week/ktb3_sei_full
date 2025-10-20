package com.example.amumal_project.user.dto;

public class UserRequestDto {

    private String email;
    private String password;
    private String nickname;
    private String profileImageUrl;

    public UserRequestDto() {

    }

    public UserRequestDto(String email, String password, String nickname, String profileImageUrl) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
