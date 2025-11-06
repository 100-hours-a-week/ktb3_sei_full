package com.example.amumal_project.api.like.repository.postLike;

import com.example.amumal_project.api.post.repository.JpaPostRepository;
import com.example.amumal_project.api.user.repository.JpaUserRepository;
import com.example.amumal_project.entity.PostEntity;
import com.example.amumal_project.entity.PostLikeEntity;
import com.example.amumal_project.entity.UserEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class AdapterPostLikeRepository implements PostLikeRepository{
    JpaPostLikeRepository jpaPostLikeRepository;
    JpaUserRepository jpaUserRepository;
    JpaPostRepository jpaPostRepository;
    public AdapterPostLikeRepository(JpaUserRepository jpaUserRepository, JpaPostRepository jpaPostRepository, JpaPostLikeRepository jpaPostLikeRepository) {
        this.jpaUserRepository = jpaUserRepository;
        this.jpaPostRepository = jpaPostRepository;
        this.jpaPostLikeRepository = jpaPostLikeRepository;
    }

    @Override
    public void createLike(Long userId, Long postId){
        UserEntity user = jpaUserRepository.findById(userId).orElseThrow();
        PostEntity post = jpaPostRepository.findById(postId).orElseThrow();
        PostLikeEntity postLikeEntity = new PostLikeEntity(user, post);
        post.setLikeCount(post.getLikeCount() + 1);
        jpaPostLikeRepository.save(postLikeEntity);
    };

    @Override
    public void deleteLike(Long userId, Long postId){
        PostEntity post = jpaPostRepository.findById(postId).orElseThrow();
        post.setLikeCount(post.getLikeCount() - 1);
        PostLikeEntity postLike = jpaPostLikeRepository.findByPostIdAndUserId(postId,userId).orElseThrow();
        jpaPostLikeRepository.deleteById(postLike.getId());
    };

    @Override
    public int countLikes(Long postId){
        return jpaPostLikeRepository.countByPostId(postId);
    };
}
