package com.example.amumal_project.api.like.controller;

import com.example.amumal_project.common.CommonResponse;
import com.example.amumal_project.common.exception.UnauthorizedException;
import com.example.amumal_project.api.like.service.CommentLikeService;
import com.example.amumal_project.api.user.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/posts/{postId}/comments/{commentId}/likes")
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    public CommentLikeController(CommentLikeService commentLikeService) {
        this.commentLikeService = commentLikeService;
    }

    @PostMapping
    public ResponseEntity<CommonResponse> pressCommentLike(@PathVariable Long postId, @PathVariable Long commentId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null){
           throw new UnauthorizedException("로그인이 필요합니다!");

        }
        boolean isLiked = commentLikeService.toggleCommentLike(commentId, loginUser.getId());

        Map<String, Object> data = new HashMap<>();
        data.put("post_id", postId);
        data.put("comment_id", commentId);
        data.put("is_liked", isLiked);

        String message = isLiked ? "like_created" : "like_deleted";

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse(message));
    }
}
