package com.example.amumal_project.api.like.service;

import com.example.amumal_project.api.like.repository.postLike.JpaPostLikeRepository;
import com.example.amumal_project.api.like.repository.postLike.PostLikeRepository;
import com.example.amumal_project.api.post.repository.PostRepository;
import com.example.amumal_project.api.user.repository.UserRepository;
import com.example.amumal_project.entity.PostEntity;
import com.example.amumal_project.entity.PostLikeEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostLikeServiceImpl implements PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JpaPostLikeRepository jpaPostLikeRepository;

    public PostLikeServiceImpl(PostLikeRepository postLikeRepository, PostRepository postRepository, UserRepository userRepository, JpaPostLikeRepository jpaPostLikeRepository) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.jpaPostLikeRepository = jpaPostLikeRepository;

    }

    @Transactional
    public boolean togglePostLike(Long postId, Long userId) {
        Optional<PostLikeEntity> exist = jpaPostLikeRepository.findByPostIdAndUserId(postId,userId);
        if (exist.isEmpty()) {
            postLikeRepository.createLike(userId, postId);
            return true;
        }else{
            postLikeRepository.deleteLike(userId, postId);
            return false;
        }
     }


}
