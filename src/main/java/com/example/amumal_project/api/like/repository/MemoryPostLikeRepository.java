package com.example.amumal_project.api.like.repository;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryPostLikeRepository implements PostLikeRepository {
    private final Map<Long, Set<Long>> postLikes = new HashMap<>();

    public boolean like(Long postId, Long userId) {
        postLikes.putIfAbsent(postId, new HashSet<>());
        Set<Long> likes = postLikes.get(postId);

        if(likes.contains(userId)){
            likes.remove(userId);
            return false;
        }else{
            likes.add(userId);
            return true;
        }
    }

    public int countLikes(Long postId) {
        return postLikes.getOrDefault(postId, Collections.emptySet()).size();
    }

    public boolean isLikedByUser(Long postId, Long userId) {
        return postLikes.getOrDefault(postId, Collections.emptySet()).contains(userId);
    }
}
