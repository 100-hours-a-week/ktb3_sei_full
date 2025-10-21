package com.example.amumal_project.like.service;

public interface PostLikeService {
    boolean likePost(Long postId, Long userId);
    int countLikes(Long postId);
    boolean isLikedByUser(Long postId, Long userId);
}
