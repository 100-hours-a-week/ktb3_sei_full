package com.example.amumal_project.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserinfoDto extends UserDto {
    private Long id;
    private String email;
    private String password;
}
