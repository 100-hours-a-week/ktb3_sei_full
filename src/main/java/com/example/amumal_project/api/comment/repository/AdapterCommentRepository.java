package com.example.amumal_project.api.comment.repository;

import com.example.amumal_project.api.comment.Comment;
import com.example.amumal_project.api.post.repository.JpaPostRepository;
import com.example.amumal_project.api.user.repository.JpaUserRepository;
import com.example.amumal_project.entity.CommentEntity;
import com.example.amumal_project.entity.PostEntity;
import com.example.amumal_project.entity.UserEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Primary
public class AdapterCommentRepository implements CommentRepository {
    private final JpaCommentRepository jpaCommentRepository;
    private final JpaPostRepository jpaPostRepository;
    private  final JpaUserRepository jpaUserRepository;

   public AdapterCommentRepository(JpaCommentRepository jpaCommentRepository,JpaPostRepository jpaPostRepository,JpaUserRepository jpaUserRepository) {
        this.jpaCommentRepository = jpaCommentRepository;
        this.jpaPostRepository = jpaPostRepository;
        this.jpaUserRepository = jpaUserRepository;
   }
   @Override
   public Comment save(Comment comment){
       UserEntity author = jpaUserRepository.findById(comment.getUserId()).orElseThrow();
       PostEntity post = jpaPostRepository.findById(comment.getPostId()).orElseThrow();
       CommentEntity commentEntity = new CommentEntity(post, author, comment.getContent());
       CommentEntity savedComment = jpaCommentRepository.save(commentEntity);
       post.setCommentCount(post.getCommentCount()+1);
       jpaPostRepository.save(post);
       return new Comment(savedComment.getId(),savedComment.getAuthor().getId(), savedComment.getPost().getId(),savedComment.getContent(),savedComment.getCreatedAt(),savedComment.getLikeCount(),savedComment.getAuthor().getNickname());
   };

   @Override
   public List<Comment> findByPostId(long postId){
       return jpaCommentRepository.findByPostId(postId).stream()
               .map(e -> new Comment(e.getId(),e.getAuthor().getId(),e.getPost().getId(),e.getContent(),e.getCreatedAt(),e.getLikeCount(), e.getAuthor().getNickname()))
               .collect(Collectors.toList());
   };
   @Override
   public void delete(Long postId,Long commentId){
       CommentEntity commentEntity = jpaCommentRepository.findById(commentId).orElseThrow();
       commentEntity.setIsDeleted(true);
       commentEntity.setDeletedAt(LocalDateTime.now());

       PostEntity post = jpaPostRepository.findById(postId).orElseThrow();
       post.setCommentCount(post.getCommentCount()-1);
       jpaPostRepository.save(post);
   };
   @Override
   public Optional<Comment> update(Long postId, Long commentId, String content){
       CommentEntity commentEntity = jpaCommentRepository.findById(commentId).orElseThrow();
       commentEntity.setContent(content);
       commentEntity.setUpdatedAt(LocalDateTime.now());
       return Optional.of(new Comment(commentEntity.getId(),commentEntity.getAuthor().getId(),commentEntity.getPost().getId(),commentEntity.getContent(),commentEntity.getCreatedAt(),commentEntity.getLikeCount(), commentEntity.getAuthor().getNickname()));
   };

    @Override
   public Optional<Comment> findById(Long id){
        return jpaCommentRepository.findById(id)
                .map(e -> new Comment(e.getId(),e.getAuthor().getId(),e.getPost().getId(),e.getContent(),e.getCreatedAt(),e.getLikeCount(),e.getAuthor().getNickname()));
    }

}
