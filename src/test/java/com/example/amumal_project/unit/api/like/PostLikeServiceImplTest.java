package com.example.amumal_project.unit.api.like;

import com.example.amumal_project.api.like.repository.postLike.JpaPostLikeRepository;
import com.example.amumal_project.api.like.repository.postLike.PostLikeRepository;
import com.example.amumal_project.api.like.service.PostLikeServiceImpl;
import com.example.amumal_project.api.post.repository.PostRepository;
import com.example.amumal_project.api.user.User;
import com.example.amumal_project.api.user.repository.UserRepository;
import com.example.amumal_project.entity.PostEntity;
import com.example.amumal_project.entity.PostLikeEntity;
import com.example.amumal_project.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class PostLikeServiceImplTest {
    @Mock
    private PostLikeRepository postLikeRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    JpaPostLikeRepository jpaPostLikeRepository;

    @InjectMocks
    private PostLikeServiceImpl postLikeServiceImpl;

    private UserEntity user = new UserEntity("aaa@email.com","password","aaa","imageUrl");
    private PostEntity post = new PostEntity(user,"title","content","imageUrl");
    private PostLikeEntity postLike = new PostLikeEntity(user, post);

    @Test
    void 게시글좋아요_생성(){
        //given
        given(jpaPostLikeRepository.findByPostIdAndUserId(any(), any())).willReturn(Optional.empty());
        //when
        boolean result = postLikeServiceImpl.togglePostLike(101L,1L);
        //then
        assertThat(result).isTrue();
    }

    @Test
    void 게시글좋아요_취소(){
        //given
        given(jpaPostLikeRepository.findByPostIdAndUserId(any(), any())).willReturn(Optional.of(postLike));
        //when
        boolean result = postLikeServiceImpl.togglePostLike(101L,1L);
        //then
        assertThat(result).isFalse();
    }

}
