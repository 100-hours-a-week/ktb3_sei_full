package com.example.amumal_project.api.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class PostRequest {
        @NonNull
        private String title;
        @NonNull
        private String content;
        @JsonProperty("image_url")
        private String imageUrl;

}
