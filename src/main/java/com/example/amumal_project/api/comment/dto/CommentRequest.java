package com.example.amumal_project.api.comment.dto;

import lombok.*;

public class CommentRequest {
    @Builder
    @Getter
    public static class CreateCommentRequest {
        @NonNull
        private String content;
    }
}
