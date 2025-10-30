package com.example.amumal_project.api.post.repository;

import com.example.amumal_project.api.post.Post;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class MemoryPostRepository implements PostRepository {

    public final Map<Long, Post> posts = new LinkedHashMap<>();
    private long sequence = 0L;

    public Post save(Post post) {
        post.setId(sequence++);
        if (post.getLikeCount() == 0) post.setLikeCount(0);
        if (post.getViewCount() == 0) post.setViewCount(0);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }

    public void delete(Long id) {
        posts.remove(id);
    }

    public Optional<Post> update(Long id, String title, String content) {
        Post post = posts.get(id);
        if(post == null) return Optional.empty();

        if(title != null && !title.isBlank()) post.setTitle(title);
        if(content != null && !content.isBlank()) post.setContent(content);
        post.setUpdatedAt(LocalDateTime.now());
        posts.put(id, post);
        return Optional.of(post);

    }


}
