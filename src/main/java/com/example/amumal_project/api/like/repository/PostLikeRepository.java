package com.example.amumal_project.api.like.repository;

public interface PostLikeRepository {
    boolean like(Long postId, Long userId);
    int countLikes(Long postId);
    boolean isLikedByUser(Long postId, Long userId);
}
