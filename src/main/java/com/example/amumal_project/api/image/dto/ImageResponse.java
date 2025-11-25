package com.example.amumal_project.api.image.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ImageResponse {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class SuccessResponse{
        private String message;
        @JsonProperty("url")
        private String imageUrl;
    }
}
