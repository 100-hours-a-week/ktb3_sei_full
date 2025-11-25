package com.example.amumal_project.api.comment.dto;

import com.example.amumal_project.api.comment.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CommentDto {
    private Long id;
    private Long userId;
    private Long postId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likeCount;
    private String nickname;

    public static  CommentDto toCommentDto(Comment comment){
        return CommentDto.builder()
                .id(comment.getId())
                .userId(comment.getUserId())
                .postId(comment.getPostId())
                .content(comment.getContent())
                .createdAt(comment.getCreated_at())
                .updatedAt(comment.getUpdated_at())
                .likeCount(comment.getLikeCount())
                .nickname(comment.getNickname())
                .build();
    }
}
