package com.example.amumal_project.post;

import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository postRepository;
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


}
