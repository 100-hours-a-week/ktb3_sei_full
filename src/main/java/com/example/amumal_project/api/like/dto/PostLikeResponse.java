package com.example.amumal_project.api.like.dto;

import com.example.amumal_project.api.post.dto.PostDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@AllArgsConstructor
public class PostLikeResponse {
    private String message;
    private PostLikeDetailData data;

    @Builder
    @Getter
    @AllArgsConstructor
    public static class PostLikeDetailData {
        private Long postId;
        @JsonProperty("isLiked")
        private boolean isLiked;

    }
}
