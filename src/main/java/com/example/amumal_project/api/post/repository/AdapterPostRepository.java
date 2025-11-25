package com.example.amumal_project.api.post.repository;

import com.example.amumal_project.api.post.Post;
import com.example.amumal_project.api.user.repository.JpaUserRepository;
import com.example.amumal_project.entity.PostEntity;
import com.example.amumal_project.entity.UserEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Primary
@Repository
public class AdapterPostRepository implements PostRepository {
    private final JpaPostRepository jpaPostRepository;
    private final JpaUserRepository jpaUserRepository;
    public AdapterPostRepository(JpaPostRepository jpaPostRepository, JpaUserRepository jpaUserRepository) {
        this.jpaPostRepository = jpaPostRepository;
        this.jpaUserRepository = jpaUserRepository;
    }
    @Override
    public Post save(Post post){
        UserEntity author = jpaUserRepository.findById(post.getUserId()).orElseThrow();
        PostEntity postEntity  = new PostEntity(author, post.getTitle(),post.getContent(),post.getImageUrl());
        PostEntity savedPost  = jpaPostRepository.save(postEntity);
        return new Post(savedPost.getId(), postEntity.getAuthor().getId(), postEntity.getTitle(),postEntity.getContent(),postEntity.getImageUrl(),postEntity.getViewCount(),postEntity.getLikeCount(), postEntity.getAuthor().getNickname(), savedPost.getCreatedAt(), savedPost.getUpdatedAt());
    };
    @Override
    public Optional<Post> findById(Long id){
        return jpaPostRepository.findById(id)
                .map(e -> new Post( e.getId(), e.getAuthor().getId(),e.getTitle(), e.getContent(),e.getImageUrl(),e.getViewCount(),e.getLikeCount(), e.getAuthor().getNickname(), e.getCreatedAt(), e.getUpdatedAt()));
    };

    @Override
    public Page<Post> findAll(Pageable pageable) {
        Page<PostEntity> entityPage = jpaPostRepository.findByIsDeletedFalse(pageable);

        return entityPage.map(e -> new Post(
                e.getId(),
                e.getAuthor().getId(),
                e.getTitle(),
                e.getContent(),
                e.getImageUrl(),
                e.getViewCount(),
                e.getLikeCount(),
                e.getAuthor().getNickname(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        ));
    }

    @Override
    public void delete(Long id){
        PostEntity postEntity = jpaPostRepository.findById(id).orElseThrow();
        postEntity.setDeleted(true);
        postEntity.setDeletedAt(LocalDateTime.now());
    };

    @Override
    public Optional<Post> update(Long id, String title, String content,String imageUrl){
        PostEntity postEntity = jpaPostRepository.findById(id).orElseThrow();
        postEntity.setTitle(title);
        postEntity.setContent(content);
        postEntity.setImageUrl(imageUrl);
        postEntity.setUpdatedAt(LocalDateTime.now());
        return Optional.of(new Post(postEntity.getId(),postEntity.getAuthor().getId(),postEntity.getTitle(),postEntity.getContent(), postEntity.getImageUrl(),postEntity.getViewCount(),postEntity.getLikeCount(), postEntity.getAuthor().getNickname() , postEntity.getCreatedAt(), postEntity.getUpdatedAt()));
    };
    @Override
    public void updateViewCount(Long id){
        PostEntity postEntity = jpaPostRepository.findById(id).orElseThrow();
        postEntity.setViewCount(postEntity.getViewCount()+1);
    };
}
