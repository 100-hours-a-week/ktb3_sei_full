package com.example.amumal_project.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private Long id;
    private String email;
    private String password;
    private String nickname;

    @JsonProperty("profile_image")
    private String profileImageUrl;

    public User(){}

    public User(Long id, String email, String password, String nickname, String profileImageUrl) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getNickname() {return nickname;}
    public void setNickname(String nickname) {this.nickname = nickname;}

    public String getProfileImageUrl() {return profileImageUrl;}
    public void setProfileImageUrl(String profileImageUrl) {this.profileImageUrl = profileImageUrl;}


}
