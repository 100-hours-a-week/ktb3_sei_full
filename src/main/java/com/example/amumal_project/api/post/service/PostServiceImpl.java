package com.example.amumal_project.api.post.service;

import com.example.amumal_project.api.post.dto.PostDto;
import com.example.amumal_project.api.post.dto.PostResponse;
import com.example.amumal_project.api.user.repository.JpaUserRepository;
import com.example.amumal_project.common.exception.AccessDeniedException;
import com.example.amumal_project.common.exception.ResourceNotFoundException;
import com.example.amumal_project.api.post.Post;
import com.example.amumal_project.api.post.repository.PostRepository;
import com.example.amumal_project.entity.PostEntity;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final JpaUserRepository jpaUserRepository;
    public PostServiceImpl(PostRepository postRepository, JpaUserRepository jpaUserRepository) {
        this.postRepository = postRepository;
        this.jpaUserRepository = jpaUserRepository;
    }

    public Post createPost(Long userId, String title, String content,String imageUrl) {
        if(title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목을 입력해주세요!");
        }
        if(content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용을 입력해주세요!");
        }
        String nickname = jpaUserRepository.findById(userId).get().getNickname();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        Post post = new Post(null, userId, title,content,imageUrl,0,0, nickname,createdAt, updatedAt);
        return postRepository.save(post);
    }


    public Map<String, Object> getPagedPosts(int page, int size, String sort) {

        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        boolean isDesc = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc");

        Sort.Direction direction = isDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortOption = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(page - 1, size, sortOption);

        Page<Post> postPage = postRepository.findAll(pageable);

        List<PostDto> dtoList = postPage.getContent()
                .stream()
                .map(post -> PostDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .nickname(post.getNickname())
                        .imageUrl(post.getImageUrl())
                        .viewCount(post.getViewCount())
                        .likeCount(post.getLikeCount())
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
                        .nickname(post.getNickname())
                        .build()
                )
                .toList();


        Map<String, Object> result = new HashMap<>();
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", postPage.getTotalPages());
        result.put("totalPosts", postPage.getTotalElements());
        result.put("sort", sort);
        result.put("posts", dtoList);

        return result;
    }


    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다."));
    }
    @Transactional
    public Post updatePost(Long id,Long userId, String title, String content,String imageUrl) {
        Post post = getPostById(id);
        if(!post.getUserId().equals(userId)) {
            throw new AccessDeniedException("본인 게시물만 수정할 수 있습니다!");
        }
        if(title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목을 입력해주세요!");
        }
        if(content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용을 입력해주세요!");
        }
        return postRepository.update(id,title,content,imageUrl)
                .orElseThrow(() -> new ResourceNotFoundException("게시글 수정에 실패했습니다."));
    }
    @Transactional
    public void deletePost(Long id,Long userId) {
        Post post = getPostById(id);

        if(!post.getUserId().equals(userId)){
            throw new AccessDeniedException("본인 게시물만 삭제할 수 있습니다!");
        }


       postRepository.delete(id);

    }

    @Transactional
    public Post increaseViewCount(Long postId) {
        Post post = getPostById(postId);
        post.setViewCount(post.getViewCount() + 1);
        postRepository.updateViewCount(postId);
        return post;
    }
}
