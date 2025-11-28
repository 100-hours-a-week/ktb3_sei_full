package com.example.amumal_project.api.like.controller;

import com.example.amumal_project.api.user.service.UserService;
import com.example.amumal_project.common.CommonResponse;
import com.example.amumal_project.common.exception.UnauthorizedException;
import com.example.amumal_project.api.like.service.CommentLikeService;
import com.example.amumal_project.api.user.User;
import com.example.amumal_project.security.details.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserService userService;

    public CommentLikeController(CommentLikeService commentLikeService, UserService userService) {
        this.commentLikeService = commentLikeService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<CommonResponse> pressCommentLike(@PathVariable Long postId, @PathVariable Long commentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        User loginUser = userService.getUserById(userDetails.getUserId());

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
