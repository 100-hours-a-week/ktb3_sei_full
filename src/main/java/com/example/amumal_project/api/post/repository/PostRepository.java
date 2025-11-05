package com.example.amumal_project.api.post.repository;

import com.example.amumal_project.api.post.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    Optional<Post> findById(Long id);
    List<Post> findAll();
    void delete(Long id);
    Optional<Post> update(Long id, String title, String content,String imageUrl);
    void updateViewCount(Long id);
}
