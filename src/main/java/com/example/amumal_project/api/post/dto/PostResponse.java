package com.example.amumal_project.api.post.dto;

import com.example.amumal_project.api.comment.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;


public class PostResponse {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class PostDetailResponse {
        private String message;
        private PostDetailData data;
    }

    @Getter
    @AllArgsConstructor
    public static class PostDetailData {
        private PostDto post;
        private List<CommentDto> comments;
    }
}
