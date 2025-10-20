package com.example.amumal_project.comment.Repository;

import com.example.amumal_project.comment.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    List<Comment> findByPostId(long postId);
    boolean delete(Long postId,Long commentId);
    Optional<Comment> update(Long postId, Long commentId, String content);

}
