package com.example.amumal_project.api.like.repository;

public interface CommentLikeRepository {
    boolean like(Long commentId, Long userId);
    boolean isLikedByUser(Long commentId, Long userId);
    int countLikes(Long commentId);
}
