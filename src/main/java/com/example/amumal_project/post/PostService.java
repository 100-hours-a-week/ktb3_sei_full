package com.example.amumal_project.post;

import com.example.amumal_project.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Long userId, String title, String content) {
        if(title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목을 입력해주세요!");
        }
        if(content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용을 입력해주세요!");
        }

        Post post = new Post(null, userId, title,content,0,null,null);
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다."));
    }

    public Post updatePost(Long id, String title, String content) {
        return postRepository.update(id,title,content)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다."));
    }

    public void deletePost(Long id) {
        postRepository.delete(id);
    }
}
