package com.example.amumal_project.api.like.service;


import com.example.amumal_project.api.comment.repository.CommentRepository;
import com.example.amumal_project.api.like.repository.commentLike.CommentLikeRepository;
import com.example.amumal_project.api.like.repository.commentLike.JpaCommentLikeRepository;
import com.example.amumal_project.api.user.repository.UserRepository;
import com.example.amumal_project.entity.CommentLikeEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentLikeServiceImpl implements CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final JpaCommentLikeRepository jpaCommentLikeRepository;

    public CommentLikeServiceImpl(CommentLikeRepository commentLikeRepository, CommentRepository commentRepository, UserRepository userRepository, JpaCommentLikeRepository jpaCommentLikeRepository) {
        this.commentLikeRepository = commentLikeRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.jpaCommentLikeRepository = jpaCommentLikeRepository;
    }

    @Transactional
    public boolean toggleCommentLike(Long commentId, Long userId){
        Optional<CommentLikeEntity> exist = jpaCommentLikeRepository.findByCommentIdAndUserId(commentId, userId);
        if(exist.isEmpty()){
            commentLikeRepository.createLike(userId, commentId);
            return true;
        }else{
            commentLikeRepository.deleteLike(userId, commentId);
            return false;
        }
    }

}
