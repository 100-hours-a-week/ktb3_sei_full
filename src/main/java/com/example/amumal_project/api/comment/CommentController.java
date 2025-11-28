package com.example.amumal_project.api.comment;

import com.example.amumal_project.api.comment.dto.CommentDto;
import com.example.amumal_project.api.comment.dto.CommentRequest;
import com.example.amumal_project.api.comment.dto.CommentResponse;
import com.example.amumal_project.api.comment.service.CommentService;
import com.example.amumal_project.api.user.service.UserService;
import com.example.amumal_project.common.CommonResponse;
import com.example.amumal_project.common.exception.UnauthorizedException;
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
        origins = {"http://127.0.0.1:5500", "http://localhost:5500"},
        allowCredentials = "true"
)
@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    public final CommentService commentService;
    public final UserService userService;

    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    //댓글 작성
    @PostMapping
    public ResponseEntity<CommonResponse> createComment(@PathVariable Long postId, @RequestBody CommentRequest.CreateCommentRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        User loginUser = userService.getUserById(userDetails.getUserId());

        String content = request.getContent();
        Comment createdComment = commentService.createComment(postId, loginUser.getId(), content);

        return ResponseEntity.ok(new CommonResponse("created_success"));
    }

    //댓글 목록 조회
    @GetMapping
    public ResponseEntity<CommentResponse.GetCommentsResponse> getComments(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);

        List<CommentDto> commentDtos = comments.stream()
                .map(CommentDto::toCommentDto)
                .toList();
        return ResponseEntity.ok(CommentResponse.GetCommentsResponse.builder().comments(commentDtos).build());
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponse> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        User loginUser = userService.getUserById(userDetails.getUserId());

        commentService.deleteComment(postId, commentId,loginUser.getId());

        return ResponseEntity.ok(new CommonResponse("delete_success"));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommonResponse> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody Map<String, String> request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        User loginUser = userService.getUserById(userDetails.getUserId());

        String content = request.get("content");
        Comment updatedComment = commentService.updateComment(postId, commentId, loginUser.getId(), content);

        return ResponseEntity.ok(new CommonResponse("update_success"));
    }
}
