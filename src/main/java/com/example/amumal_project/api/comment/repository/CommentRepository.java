package com.example.amumal_project.api.comment.repository;

import com.example.amumal_project.api.comment.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    List<Comment> findByPostId(long postId);
    void delete(Long postId,Long commentId);
    Optional<Comment> update(Long postId, Long commentId, String content);
    Optional<Comment> findById(Long commentId);
    Optional<Comment> findByPostIdAndCommentId(long postId, long commentId);
}
