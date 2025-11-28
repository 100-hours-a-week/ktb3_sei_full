package com.example.amumal_project.api.like.controller;

import com.example.amumal_project.api.like.dto.PostLikeResponse;
import com.example.amumal_project.api.user.service.UserService;
import com.example.amumal_project.common.exception.UnauthorizedException;
import com.example.amumal_project.api.like.service.PostLikeService;
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
@RequestMapping("/posts")
public class PostLikeController {
    private final PostLikeService postLikeService;
    private final UserService userService;

    public PostLikeController(PostLikeService postLikeService, UserService userService) {
        this.postLikeService = postLikeService;
        this.userService = userService;
    }

    @PostMapping("{postId}/likes")
    public ResponseEntity<PostLikeResponse> pressPostLike(@PathVariable Long postId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        User loginUser = userService.getUserById(userDetails.getUserId());


        if(loginUser == null){
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        boolean isLiked = postLikeService.togglePostLike(postId, loginUser.getId());
        String message = isLiked ? "like_created" : "like_deleted";

        PostLikeResponse.PostLikeDetailData data = new PostLikeResponse.PostLikeDetailData(postId, isLiked);

        return ResponseEntity.ok(new PostLikeResponse(message,data));
    }
}
