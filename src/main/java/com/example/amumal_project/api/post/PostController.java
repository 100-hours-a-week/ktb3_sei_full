package com.example.amumal_project.api.post;

import com.example.amumal_project.api.comment.Comment;
import com.example.amumal_project.api.comment.service.CommentService;
import com.example.amumal_project.common.exception.AccessDeniedException;
import com.example.amumal_project.common.exception.UnauthorizedException;
import com.example.amumal_project.api.like.service.PostLikeService;
import com.example.amumal_project.api.post.service.PostService;
import com.example.amumal_project.api.user.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;

    public PostController(PostService postService, CommentService commentService, PostLikeService postLikeService) {
        this.postService = postService;
        this.commentService = commentService;
        this.postLikeService = postLikeService;
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
        String imageUrl = request.get("image_url");
        Post createPost = postService.createPost(user.getId(), title, content,imageUrl);
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

        postService.deletePost(postId, user.getId());

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
        String imageUrl = request.get("image_url");

        Post post = postService.getPostById(postId);
        if(!post.getUserId().equals(user.getId())){
            throw new AccessDeniedException("본인 게시글만 수정할 수 있습니다.");
        }

        Post updatePost = postService.updatePost(postId,user.getId(), title, content,imageUrl);

        Map<String, Object> response = new HashMap<>();
        response.put("message","update_success");
        response.put("data", updatePost);
        return ResponseEntity.ok(response);
    }

    //게시글 목록조회
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "created_at,desc") String sort
    ) {
        Map<String, Object> result = postService.getPagedPosts(page, size, sort);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "fetch_success");
        response.put("data", result);
        return ResponseEntity.ok(response);
    }
    //

    //게시글 상세조회
    @GetMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> getOnePost(@PathVariable Long postId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");

        Post post = postService.increaseViewCount(postId);

        List<Comment> comments = commentService.getCommentsByPostId(postId);

        int likeCount = postLikeService.countLikes(postId);
        boolean likedByMe = loginUser !=null && postLikeService.isLikedByUser(postId, loginUser.getId());

        Map<String, Object> data = new HashMap<>();
        data.put("post", post);
        data.put("like_count", likeCount);
        data.put("is_likes", likedByMe);
        data.put("comments", comments);
        data.put("view_count", post.getViewCount());

        Map<String, Object> response = new HashMap<>();
        response.put("message","fetch_success");
        response.put("data", data);

        return ResponseEntity.ok(response);
    }
}
