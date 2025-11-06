/* package com.example.amumal_project.api.comment.repository;

import com.example.amumal_project.api.comment.Comment;
import com.example.amumal_project.api.comment.repository.CommentRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class MemoryCommentRepository implements CommentRepository {
    private final Map<Long, List<Comment>> allComments = new LinkedHashMap<>();
    private long sequence = 0L;

    public Comment save(Comment comment) {
        comment.setId(++sequence);
        comment.setCreated_at(LocalDateTime.now());
        comment.setUpdated_at(LocalDateTime.now());

        allComments.computeIfAbsent(comment.getPostId(),k -> new ArrayList<>()).add(comment);
        return comment;
    }

    public List<Comment> findByPostId(long postId) {
        return allComments.getOrDefault(postId, new ArrayList<>());
    }

    public void delete(Long postId,Long commentId) {
        List<Comment> comments = allComments.get(postId);
        if (comments == null) {
        }
        comments.removeIf(c -> c.getId().equals(commentId));
    }

    public Optional<Comment> update(Long postId, Long commentId, String content) {
        List<Comment> comments = allComments.get(postId);
        if (comments == null) {
            return Optional.empty();
        }
        for(Comment c : comments) {
            if(c.getId().equals(commentId)) {
                if(content !=null && !content.isBlank()) {
                    c.setContent(content);
                    c.setUpdated_at(LocalDateTime.now());
                }
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }



}
*/