package com.example.amumal_project.api.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class UserResponse {
    @Builder
    @Getter
    public static class GetUsersResponse {
        private List<UserDto> users;
    }



}
