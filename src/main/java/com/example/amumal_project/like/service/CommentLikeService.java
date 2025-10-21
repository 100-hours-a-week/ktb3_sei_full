package com.example.amumal_project.like.service;

public interface CommentLikeService {
    boolean likeComment(Long commentId, Long userId);
    boolean isLiked(Long commentId, Long userId);
}
