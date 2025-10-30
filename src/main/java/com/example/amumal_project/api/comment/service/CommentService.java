package com.example.amumal_project.api.comment.service;

import com.example.amumal_project.api.comment.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(Long postId, Long userId, String content);
    List<Comment> getCommentsByPostId(Long postId);
    void deleteComment(Long postId, Long commentId, Long userId);
    Comment updateComment(Long postId, Long commentId, Long userId, String content);

}
