package com.example.amumal_project.api.post;

import com.example.amumal_project.api.post.repository.PostRepository;
import com.example.amumal_project.api.post.service.PostServiceImpl;
import com.example.amumal_project.api.user.User;
import com.example.amumal_project.api.user.repository.JpaUserRepository;
import com.example.amumal_project.common.exception.AccessDeniedException;
import com.example.amumal_project.common.exception.ResourceNotFoundException;
import com.example.amumal_project.entity.PostEntity;
import com.example.amumal_project.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private JpaUserRepository jpaUserRepository;
    @InjectMocks
    private PostServiceImpl postServiceImpl;


    @Test
    void 게시글작성_성공(){
        //given
        UserEntity user = new UserEntity("aaa@email.com","password","aaa","image/profile_image.png");
        Post post = new Post(101L,1L,"tile","content","/images/post.png",0,0,0,"author_nickname",
                LocalDateTime.of(2025, 12, 4, 15, 23, 10),LocalDateTime.of(2025, 12, 4, 15, 23, 10));
        given(jpaUserRepository.findById(Mockito.anyLong())).willReturn(Optional.of(user));
        given(postRepository.save(Mockito.any())).willReturn(post);
        //when
        Post result = postServiceImpl.createPost(1L,"title","content","/images/post.png");
        //then
        assertThat(result).isEqualTo(post);
    }

    @Test
    void 게시글작성_제목Null(){
        //given
        //when
        assertThatThrownBy(()->postServiceImpl.createPost(1L,null,"content","/images/post.png"))
                .isInstanceOf(IllegalArgumentException.class);
        //then
        verify(postRepository, never()).save(Mockito.any());
    }

    @Test
    void 게시글작성_내용Null(){
        //given
        //when
        assertThatThrownBy(()->postServiceImpl.createPost(1L,"title",null,"/images/post.png"))
                .isInstanceOf(IllegalArgumentException.class);
        //then
        verify(postRepository, never()).save(Mockito.any());
    }


    @Test
    void 게시물목록_조회_성공(){
        //given
         //when
        //then

    }

    @Test
    void 게시물목록_조회_실패(){
        //given

        //when

        //then

    }

    @Test
    void 아이디로게시글조회_성공(){
        Post post = new Post(101L,1L,"tile","content","/images/post.png",0,0,0,"author_nickname",
                LocalDateTime.of(2025, 12, 4, 15, 23, 10),LocalDateTime.of(2025, 12, 4, 15, 23, 10));
        given(postRepository.findById(Mockito.anyLong())).willReturn(Optional.of(post));
        //when
        Post result = postServiceImpl.getPostById(101L);
        //then
        assertThat(result).isEqualTo(post);
    }


    @Test
    void 아이디로게시글조회_실패(){
        //given
        given(postRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());
        //when
        assertThatThrownBy(()->postServiceImpl.getPostById(Mockito.anyLong()))
                .isInstanceOf(ResourceNotFoundException.class);
        //then
        verify(postRepository, times(1)).findById(Mockito.anyLong());
    }

    @Test
    void 게시글업데이트_성공(){
        //given
        Post post = new Post(101L,1L,"tile","content","/images/post.png",0,0,0,"author_nickname",
                LocalDateTime.of(2025, 12, 4, 15, 23, 10),LocalDateTime.of(2025, 12, 4, 15, 23, 10));
        given(postRepository.findById(Mockito.anyLong())).willReturn(Optional.of(post));
        given(postRepository.update(Mockito.anyLong(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).willReturn(Optional.of(post));
        //when
        Post result = postServiceImpl.updatePost(101L,1L,"title","content","/images/post.png");
        //then
        assertThat(result).isEqualTo(post);
    }

    @Test
    void 게시글업데이트_타인게시글(){
        //given
        Post post = new Post(101L,2L,"tile","content","/images/post.png",0,0,0,"author_nickname",
                LocalDateTime.of(2025, 12, 4, 15, 23, 10),LocalDateTime.of(2025, 12, 4, 15, 23, 10));
        given(postRepository.findById(Mockito.anyLong())).willReturn(Optional.of(post));
        //when
        assertThatThrownBy(()->postServiceImpl.updatePost(101L,1L,"title","content","/images/post.png"))
                .isInstanceOf(AccessDeniedException.class);
        //then
        verify(postRepository, never()).update(Mockito.anyLong(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString());
    }

    @Test
    void 게시글업데이트_제목Null(){
        //given
        Post post = new Post(101L,1L,"tile","content","/images/post.png",0,0,0,"author_nickname",
                LocalDateTime.of(2025, 12, 4, 15, 23, 10),LocalDateTime.of(2025, 12, 4, 15, 23, 10));
        given(postRepository.findById(Mockito.anyLong())).willReturn(Optional.of(post));
        //when
        assertThatThrownBy(()->postServiceImpl.updatePost(101L,1L,null,"content","/images/post.png"))
                .isInstanceOf(IllegalArgumentException.class);
        //then
        verify(postRepository, never()).update(Mockito.anyLong(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString());

    }

    @Test
    void 게시글업데이트_내용Null(){
        Post post = new Post(101L,1L,"tile","content","/images/post.png",0,0,0,"author_nickname",
                LocalDateTime.of(2025, 12, 4, 15, 23, 10),LocalDateTime.of(2025, 12, 4, 15, 23, 10));
        given(postRepository.findById(Mockito.anyLong())).willReturn(Optional.of(post));
        //when
        assertThatThrownBy(()->postServiceImpl.updatePost(101L,1L,"title",null,"/images/post.png"))
                .isInstanceOf(IllegalArgumentException.class);
        //then
        verify(postRepository, never()).update(Mockito.anyLong(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString());

    }

    @Test
    void 게시글업데이트_실패(){
        //given
        Post post = new Post(101L,1L,"tile","content","/images/post.png",0,0,0,"author_nickname",
                LocalDateTime.of(2025, 12, 4, 15, 23, 10),LocalDateTime.of(2025, 12, 4, 15, 23, 10));
        given(postRepository.findById(Mockito.anyLong())).willReturn(Optional.of(post));
        given(postRepository.update(Mockito.anyLong(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).willReturn(Optional.empty());

        //when
        assertThatThrownBy(()->postServiceImpl.updatePost(101L,1L,"title","content","/images/post.png"))
        .isInstanceOf(ResourceNotFoundException.class);
        //then
        verify(postRepository,times(1)).update(Mockito.anyLong(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString());
    }

    @Test
    void 게시글삭제_성공(){
        //given
        Post post = new Post(101L,1L,"tile","content","/images/post.png",0,0,0,"author_nickname",
                LocalDateTime.of(2025, 12, 4, 15, 23, 10),LocalDateTime.of(2025, 12, 4, 15, 23, 10));
        given(postRepository.findById(Mockito.anyLong())).willReturn(Optional.of(post));
        //when
        postServiceImpl.deletePost(101L,1L);
        //then
        verify(postRepository).delete(101L);
    }

    @Test
    void 게시글삭제_타인게시글(){
        //given
        Post post = new Post(101L,2L,"tile","content","/images/post.png",0,0,0,"author_nickname",
                LocalDateTime.of(2025, 12, 4, 15, 23, 10),LocalDateTime.of(2025, 12, 4, 15, 23, 10));
        given(postRepository.findById(Mockito.anyLong())).willReturn(Optional.of(post));
        //when
        assertThatThrownBy(()->postServiceImpl.deletePost(101L,1L))
        .isInstanceOf(AccessDeniedException.class);
        //then
        verify(postRepository, never()).delete(Mockito.anyLong());

    }

    @Test
    void 조회수증가_성공(){
        //given
        Post post = new Post(101L,1L,"tile","content","/images/post.png",0,0,0,"author_nickname",
                LocalDateTime.of(2025, 12, 4, 15, 23, 10),LocalDateTime.of(2025, 12, 4, 15, 23, 10));
        given(postRepository.findById(Mockito.anyLong())).willReturn(Optional.of(post));
        //when
        Post result = postServiceImpl.increaseViewCount(101L);
        //then
        assertThat(result.getViewCount()).isEqualTo(post.getCommentCount()+1);
    }

}
