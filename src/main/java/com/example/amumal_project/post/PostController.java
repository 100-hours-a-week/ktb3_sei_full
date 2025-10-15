package com.example.amumal_project.post;

import com.example.amumal_project.common.exception.AccessDeniedException;
import com.example.amumal_project.common.exception.ResourceNotFoundException;
import com.example.amumal_project.common.exception.UnauthorizedException;
import com.example.amumal_project.user.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //게시글 작성
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPost(@RequestBody Map<String, String> request, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if(user == null){
            throw new UnauthorizedException("잘못된 접근 입니다.");
        }
        String title = request.get("title");
        String content = request.get("content");

        Post createPost = postService.createPost(user.getId(), title, content);
        Map<String, Object> response = new HashMap<>();
        response.put("message","create_success");
        response.put("data", createPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> deletePost(@PathVariable Long postId, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if(user == null){
            throw new UnauthorizedException("잘못된 접근입니다.");
        }

        Post post = postService.getPostById(postId);
        if(!post.getUserId().equals(user.getId())){
            throw new AccessDeniedException("본인 게시글만 삭제할 수 있습니다.");
        }

        postService.deletePost(postId);

        Map<String, Object> response = new HashMap<>();
        response.put("message","delete_success");
        response.put("data", null);
        return ResponseEntity.ok(response);
    }

    //게시글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> updatePost(@PathVariable Long postId, @RequestBody Map<String, String> request, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if(user == null){
            throw new UnauthorizedException("잘못된 접근입니다.");
        }
        String title = request.get("title");
        String content = request.get("content");

        Post post = postService.getPostById(postId);
        if(!post.getUserId().equals(user.getId())){
            throw new AccessDeniedException("본인 게시글만 수정할 수 있습니다.");
        }

        Post updatePost = postService.updatePost(postId, title, content);

        Map<String, Object> response = new HashMap<>();
        response.put("message","update_success");
        response.put("data", updatePost);
        return ResponseEntity.ok(response);
    }
}
