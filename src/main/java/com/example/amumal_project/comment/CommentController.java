package com.example.amumal_project.comment;

import com.example.amumal_project.common.exception.ResourceNotFoundException;
import com.example.amumal_project.common.exception.UnauthorizedException;
import com.example.amumal_project.user.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    public final CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //댓글 작성
    @PostMapping
    public ResponseEntity<Map<String, Object>> createComment(@PathVariable Long postId, @RequestBody Map<String, String> request, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null){
            throw new UnauthorizedException("잘못된 접근입니다.");
        }
        String content = request.get("content");
        Comment createdComment = commentService.createComment(postId, loginUser.getId(), content);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "created_success");
        response.put("data", createdComment);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //댓글 목록 조회
    @GetMapping
    public ResponseEntity<Map<String, Object>> getComments(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "fetch_success");
        response.put("data", comments);
        return ResponseEntity.ok(response);
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long postId, @PathVariable Long commentId,HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null){
            throw new UnauthorizedException("잘못된 접근입니다.");
        }

        commentService.deleteComment(postId, commentId,loginUser.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "delete_success");
        response.put("data", null);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody Map<String, String> request, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null){
            throw new UnauthorizedException("잘못된 접근입니다.");
        }

        String content = request.get("content");
        Comment updatedComment = commentService.updateComment(postId, commentId, loginUser.getId(), content);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "update_success");
        response.put("data", updatedComment);
        return ResponseEntity.ok(response);
    }
}
