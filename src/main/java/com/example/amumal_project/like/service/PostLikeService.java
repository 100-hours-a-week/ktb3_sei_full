package com.example.amumal_project.like.service;

import com.example.amumal_project.like.repository.PostLikeRepository;
import com.example.amumal_project.post.Post;
import org.springframework.stereotype.Service;

@Service
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;

    public PostLikeService(PostLikeRepository postLikeRepository) {
        this.postLikeRepository = postLikeRepository;
    }

    public boolean likePost(Long postId, Long userId) {
        return postLikeRepository.like(userId, postId);
    }

    public int countLikes(Long postId) {
        return postLikeRepository.countLikes(postId);
    }

    public boolean isLikedByUser(Long postId, Long userId) {
        return postLikeRepository.isLikedByUser(postId, userId);
    }
}
