package com.example.amumal_project.api.post.service;

import com.example.amumal_project.common.exception.AccessDeniedException;
import com.example.amumal_project.common.exception.ResourceNotFoundException;
import com.example.amumal_project.api.post.Post;
import com.example.amumal_project.api.post.repository.PostRepository;
import com.example.amumal_project.entity.PostEntity;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Long userId, String title, String content,String imageUrl) {
        if(title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목을 입력해주세요!");
        }
        if(content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용을 입력해주세요!");
        }

        Post post = new Post(null, userId, title,content,imageUrl,0,0);
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Map<String, Object> getPagedPosts(int page,int size, String sort){
        List<Post> allPosts = postRepository.findAll();

        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        boolean isDesc = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc");

        Comparator<Post> comparator;

        switch (sortField) {
            case "title":
                comparator = Comparator.comparing(Post::getTitle, String.CASE_INSENSITIVE_ORDER);
                break;
            case "updatedAt":
            case "updated_at":
                comparator = Comparator.comparing(Post::getUpdatedAt);
                break;
            case "createdAt":
            case "created_at":
            default:
                comparator = Comparator.comparing(Post::getCreatedAt);
                break;
        }

        if (isDesc) {
            comparator = comparator.reversed();
        }

        allPosts.sort(comparator);

        int totalPosts = allPosts.size();
        int totalPages = (int) Math.ceil((double) totalPosts / size);
        int fromIndex = Math.max(0, (page - 1) * size);
        int toIndex = Math.min(fromIndex + size, totalPosts);

        List<Post> pagedPosts = allPosts.subList(fromIndex, toIndex);

        Map<String, Object> result = new HashMap<>();
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", totalPages);
        result.put("totalPosts", totalPosts);
        result.put("sort", sort);
        result.put("posts", pagedPosts);

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
