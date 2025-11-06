package com.example.amumal_project.api.like.repository.commentLike;

import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository {
   /*
   boolean like(Long commentId, Long userId);
   boolean isLikedByUser(Long commentId, Long userId);
   int countLikes(Long commentId);
   */

    void createLike(Long userId, Long commentId);
    void deleteLike(Long userId, Long commentId);
    int countLikes(Long commentId);
}
