package com.example.amumal_project.api.like.repository.postLike;

import com.example.amumal_project.entity.CommentLikeEntity;
import com.example.amumal_project.entity.PostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaPostLikeRepository extends JpaRepository<PostLikeEntity, Long> {
    Optional<PostLikeEntity> findByPostIdAndUserId(Long postId, Long userId);
    int countByPostId(Long postId);
}
