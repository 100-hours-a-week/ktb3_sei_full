package com.example.amumal_project.api.like.repository.commentLike;

import com.example.amumal_project.entity.CommentLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaCommentLikeRepository extends JpaRepository<CommentLikeEntity, Long> {
    Optional<CommentLikeEntity> findByCommentIdAndUserId(Long commentId, Long userId);
    int countByCommentId(Long commentId);
}
