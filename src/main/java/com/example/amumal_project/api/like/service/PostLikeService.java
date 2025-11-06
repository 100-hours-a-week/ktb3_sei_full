package com.example.amumal_project.api.like.service;

public interface PostLikeService {
    boolean togglePostLike(Long postId, Long userId);
}
