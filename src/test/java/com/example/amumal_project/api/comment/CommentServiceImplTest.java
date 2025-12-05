package com.example.amumal_project.api.comment;

import com.example.amumal_project.api.comment.repository.CommentRepository;
import com.example.amumal_project.api.comment.service.CommentServiceImpl;
import com.example.amumal_project.api.like.repository.commentLike.CommentLikeRepository;
import com.example.amumal_project.api.user.User;
import com.example.amumal_project.api.user.repository.JpaUserRepository;
import com.example.amumal_project.common.exception.AccessDeniedException;
import com.example.amumal_project.common.exception.ResourceNotFoundException;
import com.example.amumal_project.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentLikeRepository commentLikeRepository;
    @Mock
    private JpaUserRepository jpaUserRepository;
    @InjectMocks
    private CommentServiceImpl commentServiceImpl;

    private Comment comment = new Comment(11L,1L,101L,"content",LocalDateTime.of(2025, 12, 4, 15, 23, 10)
            ,0,"aaa","image/profile_image.png");
    private List<Comment> comments = List.of(comment);

    @Test
    void 댓글생성_성공(){
        //given
        UserEntity user = new UserEntity("aaa@email.com","password","aaa","image/profile_image.png");
        given(jpaUserRepository.findById(1L)).willReturn(Optional.of(user));
        given(commentRepository.save(Mockito.any())).willReturn(comment);
        //when
        Comment result = commentServiceImpl.createComment(101L,1L,"content");
        //then
        assertThat(result.getContent()).isEqualTo("content");
        assertThat(result.getPostId()).isEqualTo(101L);
        assertThat(result.getUserId()).isEqualTo(1L);
    }

    @Test
    void 댓글생성_내용Null(){
        //given
        //when
        assertThatThrownBy(()->commentServiceImpl.createComment(101L,1L,null))
                .isInstanceOf(IllegalArgumentException.class);
        //then
        verify(commentRepository, never()).save(Mockito.any());
    }

    @Test
    void 게시글아이디로_댓글목록조회_성공(){
        //given
        UserEntity user = new UserEntity("aaa@email.com","password","aaa","image/profile_image.png");
        given(commentRepository.findByPostId(101L)).willReturn(comments);
        given(jpaUserRepository.findById(1L)).willReturn(Optional.of(user));
        given(commentLikeRepository.countLikes(Mockito.any())).willReturn(anyInt());
        //when
        List<Comment> result =  commentServiceImpl.getCommentsByPostId(101L);
        //then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void 댓글삭제_성공(){
        //given
         given(commentRepository.findByPostIdAndCommentId(anyLong(),anyLong())).willReturn(Optional.of(comment));

        //when
        commentServiceImpl.deleteComment(101L,11L,1L);
        //then
        verify(commentRepository).delete(101L,11L);
    }

    @Test
    void 댓글삭제_댓글을찾을수없음(){
        //given
        given(commentRepository.findByPostIdAndCommentId(anyLong(),anyLong())).willReturn(Optional.empty());
        //when
        assertThatThrownBy(()->commentServiceImpl.deleteComment(101L,11L,1L))
        .isInstanceOf(ResourceNotFoundException.class);
        //then
        verify(commentRepository, never()).delete(anyLong(),anyLong());
    }

    @Test
    void 댓글삭제_타인댓글(){
        //given
        given(commentRepository.findByPostIdAndCommentId(anyLong(),anyLong())).willReturn(Optional.of(comment));
        //when
        assertThatThrownBy(()->commentServiceImpl.deleteComment(101L,11L,2L))
        .isInstanceOf(AccessDeniedException.class);
        //then
        verify(commentRepository, never()).delete(anyLong(),anyLong());
    }

    @Test
    void 댓글수정_성공(){
        //given
        given(commentRepository.findByPostIdAndCommentId(anyLong(),anyLong())).willReturn(Optional.of(comment));
        given(commentRepository.update(anyLong(),anyLong(),anyString())).willReturn(Optional.of(comment));
        //when
        Comment result = commentServiceImpl.updateComment(101L,11L,1L,"content");
        //then
        assertThat(result.getContent()).isEqualTo("content");
        assertThat(result.getPostId()).isEqualTo(101L);
    }

    @Test
    void 댓글수정_댓글내용Null(){
        //given

        //when
        assertThatThrownBy(()->commentServiceImpl.updateComment(101L,11L,1L,null))
        .isInstanceOf(IllegalArgumentException.class);
        //then
        verify(commentRepository, never()).update(anyLong(),anyLong(),anyString());
    }

    @Test
    void 댓글수정_댓글을찾을수없음(){
        //given
        given(commentRepository.findByPostIdAndCommentId(anyLong(),anyLong())).willReturn(Optional.empty());
        //when
        assertThatThrownBy(()->commentServiceImpl.updateComment(101L,11L,1L,"content"))
        .isInstanceOf(ResourceNotFoundException.class);
        //then
        verify(commentRepository, never()).update(anyLong(),anyLong(),anyString());
    }

    @Test
    void 댓글수정_타인댓글(){
        //given
        given(commentRepository.findByPostIdAndCommentId(anyLong(),anyLong())).willReturn(Optional.of(comment));
        //when
        assertThatThrownBy(()->commentServiceImpl.updateComment(101L,11L,2L,"content"))
                .isInstanceOf(AccessDeniedException.class);
        //then
        verify(commentRepository, never()).update(anyLong(),anyLong(),anyString());

    }

    @Test
    void 댓글수정_실패(){
        //given
        given(commentRepository.findByPostIdAndCommentId(anyLong(),anyLong())).willReturn(Optional.of(comment));
        given(commentRepository.update(anyLong(),anyLong(),anyString())).willReturn(Optional.empty());
        //when
        assertThatThrownBy(()->commentServiceImpl.updateComment(101L,11L,1L,"content"))
                .isInstanceOf(ResourceNotFoundException.class);
        //then
        verify(commentRepository).update(101L,11L,"content");    }
}
