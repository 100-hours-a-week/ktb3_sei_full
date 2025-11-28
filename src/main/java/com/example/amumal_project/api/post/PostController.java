package com.example.amumal_project.api.post;

import com.example.amumal_project.api.comment.Comment;
import com.example.amumal_project.api.comment.dto.CommentDto;
import com.example.amumal_project.api.comment.service.CommentService;
import com.example.amumal_project.api.post.dto.PostDto;
import com.example.amumal_project.api.post.dto.PostRequest;
import com.example.amumal_project.api.post.dto.PostResponse;
import com.example.amumal_project.api.user.service.UserService;
import com.example.amumal_project.common.CommonResponse;
import com.example.amumal_project.common.exception.AccessDeniedException;
import com.example.amumal_project.common.exception.UnauthorizedException;
import com.example.amumal_project.api.like.service.PostLikeService;
import com.example.amumal_project.api.post.service.PostService;
import com.example.amumal_project.api.user.User;
import com.example.amumal_project.security.details.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(
        origins = "http://127.0.0.1:5500",
        allowCredentials = "true"
)
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;
    private final UserService userService;

    public PostController(PostService postService, CommentService commentService, PostLikeService postLikeService, UserService userService) {
        this.postService = postService;
        this.commentService = commentService;
        this.postLikeService = postLikeService;
        this.userService = userService;
    }

    //게시글 작성
    @PostMapping
    public ResponseEntity<CommonResponse> createPost(@RequestBody PostRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        User user = userService.getUserById(userDetails.getUserId());

        Post createPost = postService.createPost(
                user.getId(),
                request.getTitle(),
                request.getContent(),
                request.getImageUrl()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse("create_success"));
    }

    //게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponse> deletePost(@PathVariable Long postId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        User user = userService.getUserById(userDetails.getUserId());

        Post post = postService.getPostById(postId);

        postService.deletePost(postId, user.getId());

        return ResponseEntity.ok(new CommonResponse("delete_success"));
    }

    //게시글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<CommonResponse> updatePost(@PathVariable Long postId, @RequestBody PostRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        User user = userService.getUserById(userDetails.getUserId());

        Post post = postService.getPostById(postId);
        if(!post.getUserId().equals(user.getId())){
            throw new AccessDeniedException("본인 게시글만 수정할 수 있습니다.");
        }

        Post updatePost = postService.updatePost(postId,user.getId(), request.getTitle(), request.getContent(),request.getImageUrl());

        return ResponseEntity.ok(new CommonResponse("update_success"));
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
    public ResponseEntity<PostResponse.PostDetailResponse> getOnePost(@PathVariable Long postId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        User loginUser = userService.getUserById(userDetails.getUserId());

        Post post = postService.increaseViewCount(postId);
        PostDto postDto = PostDto.toPostDto(post);

        List<Comment> comments = commentService.getCommentsByPostId(postId);
        List<CommentDto> commentDtos = comments.stream()
                .map(CommentDto::toCommentDto)
                .toList();

        int likeCount = post.getLikeCount();

        PostResponse.PostDetailData data =
                new PostResponse.PostDetailData(postDto, commentDtos);

        return ResponseEntity.ok(new PostResponse.PostDetailResponse("fetch_success",data));
    }
}
