package com.example.amumal_project.comment.service;

import com.example.amumal_project.comment.Comment;
import com.example.amumal_project.comment.repository.CommentRepository;
import com.example.amumal_project.common.exception.AccessDeniedException;
import com.example.amumal_project.common.exception.ResourceNotFoundException;
import com.example.amumal_project.like.repository.CommentLikeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    public CommentServiceImpl(CommentRepository commentRepository, CommentLikeRepository commentLikeRepository) {
        this.commentRepository = commentRepository;
        this.commentLikeRepository = commentLikeRepository;
    }

    public Comment createComment(Long postId, Long userId, String content) {
        if(content == null || content.isBlank()) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요!");
        }

        Comment comment = new Comment(null, userId,postId, content,null,0);
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        comments.forEach(c -> c.setLikeCount(commentLikeRepository.countLikes(c.getId())));
        return comments;
    }

    public void deleteComment(Long postId, Long commentId, Long userId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        if(comments.isEmpty()) {
            throw new ResourceNotFoundException("댓글을 찾을 수 없습니다.");
        }
        Comment comment = comments.stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("댓글을 찾을 수 없습니다."));

        if(!comment.getUserId().equals(userId)) {
            throw new AccessDeniedException("본인 댓글만 삭제할 수 있습니다!");
        }
         commentRepository.delete(postId, commentId);
    }

    public Comment updateComment(Long postId, Long commentId, Long userId, String content) {
        if(content == null || content.isBlank()) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요!");
        }
        List<Comment> comments = commentRepository.findByPostId(postId);
        if(comments.isEmpty()) {
            throw new ResourceNotFoundException("댓글을 찾을 수 없습니다.");
        }
        Comment comment = comments.stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("댓글을 찾을 수 없습니다."));

        if(!comment.getUserId().equals(userId)) {
            throw new AccessDeniedException("본인 댓글만 수정할 수 있습니다!");
        }
        return commentRepository.update(postId, commentId, content)
                .orElseThrow(() -> new ResourceNotFoundException("수정을 실패하였습니다."));
    }
}
