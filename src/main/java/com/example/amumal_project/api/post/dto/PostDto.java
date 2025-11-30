package com.example.amumal_project.api.post.dto;

import com.example.amumal_project.api.post.Post;
import com.example.amumal_project.api.post.repository.JpaPostRepository;
import com.example.amumal_project.api.user.User;
import com.example.amumal_project.api.user.repository.AdapterUserRepository;
import com.example.amumal_project.api.user.repository.JpaUserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostDto {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    @JsonProperty("image_url")
    private String imageUrl;
    private int likeCount;
    private int viewCount;
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String nickname;
    private String profileImageUrl;

    public static PostDto toPostDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .nickname(post.getNickname())
                .imageUrl(post.getImageUrl())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

}


