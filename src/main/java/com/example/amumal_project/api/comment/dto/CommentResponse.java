package com.example.amumal_project.api.comment.dto;

import com.example.amumal_project.api.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class CommentResponse {
    @Getter
    @Builder
    public static class GetCommentsResponse {
        private List<CommentDto> comments;
    }

}
