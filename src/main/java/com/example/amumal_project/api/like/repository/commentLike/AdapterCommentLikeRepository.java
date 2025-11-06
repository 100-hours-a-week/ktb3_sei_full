package com.example.amumal_project.api.like.repository.commentLike;

import com.example.amumal_project.api.comment.repository.JpaCommentRepository;
import com.example.amumal_project.api.user.repository.JpaUserRepository;
import com.example.amumal_project.entity.CommentEntity;
import com.example.amumal_project.entity.CommentLikeEntity;
import com.example.amumal_project.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public class AdapterCommentLikeRepository implements CommentLikeRepository {
    JpaCommentLikeRepository jpaCommentLikeRepository;
    JpaCommentRepository jpaCommentRepository;
    JpaUserRepository jpaUserRepository;

    public AdapterCommentLikeRepository(JpaCommentLikeRepository jpaCommentLikeRepository,JpaCommentRepository jpaCommentRepository,JpaUserRepository jpaUserRepository) {
        this.jpaCommentLikeRepository = jpaCommentLikeRepository;
        this.jpaCommentRepository = jpaCommentRepository;
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public void createLike(Long userId, Long commentId) {
        UserEntity user = jpaUserRepository.findById(userId).orElseThrow();
        CommentEntity comment = jpaCommentRepository.findById(commentId).orElseThrow();
        CommentLikeEntity commentLikeEntity = new CommentLikeEntity(comment,user);
        comment.setLikeCount(comment.getLikeCount()+1);
        jpaCommentLikeRepository.save(commentLikeEntity);

    }
    @Override
    public void deleteLike(Long userId, Long commentId) {
        CommentEntity comment = jpaCommentRepository.findById(commentId).orElseThrow();
        comment.setLikeCount(comment.getLikeCount()-1);
        CommentLikeEntity commentLike = jpaCommentLikeRepository.findByCommentIdAndUserId(commentId,userId).orElseThrow();
        jpaCommentLikeRepository.deleteById(commentLike.getId());
    }

    @Override
    public int countLikes(Long commentId) {
        return jpaCommentLikeRepository.countByCommentId(commentId);
    }
}

