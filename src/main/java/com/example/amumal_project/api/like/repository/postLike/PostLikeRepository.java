package com.example.amumal_project.api.like.repository.postLike;

public interface PostLikeRepository {
    void createLike(Long userId, Long postId);
    void deleteLike(Long userId, Long postId);
    int countLikes(Long postId);
}
