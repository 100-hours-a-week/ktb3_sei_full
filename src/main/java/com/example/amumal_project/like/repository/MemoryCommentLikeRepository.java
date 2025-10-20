package com.example.amumal_project.like.repository;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryCommentLikeRepository implements CommentLikeRepository {

    private final Map<Long, Set<Long>> commentLikes = new HashMap<>();

    public boolean like(Long commentId, Long userId){
        commentLikes.putIfAbsent(commentId, new HashSet<>());
        Set<Long> userLikes = commentLikes.get(commentId);

        if (userLikes.contains(userId)) {
            userLikes.remove(userId);
            return false;
        } else {
            userLikes.add(userId);
            return true;
        }
    }

    public boolean isLikedByUser(Long commentId, Long userId){
        return commentLikes.getOrDefault(commentId, Collections.emptySet()).contains(userId);
    }

    public int countLikes(Long commentId){
        return commentLikes.getOrDefault(commentId, Collections.emptySet()).size();
    }
}
