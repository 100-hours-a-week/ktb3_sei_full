package com.example.amumal_project.api.post.service;

import com.example.amumal_project.api.post.Post;

import java.util.List;
import java.util.Map;

public interface PostService {
    Post createPost(Long userId, String title, String content);
    List<Post> getAllPosts();
    Map<String, Object> getPagedPosts(int page, int size, String sort);
    Post getPostById(Long id);
    Post updatePost(Long id,Long userId, String title, String content);
    void deletePost(Long id,Long userId);
    Post increaseViewCount(Long postId);
}
