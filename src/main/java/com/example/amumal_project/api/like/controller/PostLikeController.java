package com.example.amumal_project.api.like.controller;

import com.example.amumal_project.common.exception.UnauthorizedException;
import com.example.amumal_project.api.like.service.PostLikeService;
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
@RequestMapping("/posts")
public class PostLikeController {
    private final PostLikeService postLikeService;

    public PostLikeController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    @PostMapping("{postId}/likes")
    public ResponseEntity<Map<String, Object>> pressPostLike(@PathVariable Long postId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null){
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        boolean isLiked = postLikeService.togglePostLike(postId, loginUser.getId());
        String message = isLiked ? "like_created" : "like_deleted";

        Map<String, Object> data = new HashMap<>();
        data.put("postId", postId);
        data.put("isLiked", isLiked);

        Map<String, Object> response = new HashMap<>();

        response.put("message",message);
        response.put("data", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
