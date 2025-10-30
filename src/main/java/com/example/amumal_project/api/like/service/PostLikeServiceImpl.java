package com.example.amumal_project.api.like.service;

import com.example.amumal_project.api.like.repository.PostLikeRepository;
import org.springframework.stereotype.Service;

@Service
public class PostLikeServiceImpl implements PostLikeService {
    private final PostLikeRepository postLikeRepository;

    public PostLikeServiceImpl(PostLikeRepository postLikeRepository) {
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
