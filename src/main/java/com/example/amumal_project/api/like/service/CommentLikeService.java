package com.example.amumal_project.api.like.service;

public interface CommentLikeService {
    boolean toggleCommentLike(Long commentId, Long userId);
}
