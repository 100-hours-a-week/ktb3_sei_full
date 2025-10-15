package com.example.amumal_project.like.service;


import com.example.amumal_project.like.repository.CommentLikeRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;

    public CommentLikeService(CommentLikeRepository commentLikeRepository) {
        this.commentLikeRepository = commentLikeRepository;
    }

    public boolean likeComment(Long commentId, Long userId) {
        return commentLikeRepository.like(commentId, userId);
    }

    public boolean isLiked(Long commentId, Long userId) {
        return commentLikeRepository.isLikedByUser(commentId, userId);
    }

}
