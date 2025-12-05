package com.example.amumal_project.api.like;

import com.example.amumal_project.api.comment.repository.CommentRepository;
import com.example.amumal_project.api.like.repository.commentLike.CommentLikeRepository;
import com.example.amumal_project.api.like.repository.commentLike.JpaCommentLikeRepository;
import com.example.amumal_project.api.like.service.CommentLikeServiceImpl;
import com.example.amumal_project.api.user.repository.UserRepository;
import com.example.amumal_project.entity.CommentEntity;
import com.example.amumal_project.entity.CommentLikeEntity;
import com.example.amumal_project.entity.PostEntity;
import com.example.amumal_project.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CommentLikeServiceImplTest {

    @Mock
    private CommentLikeRepository commentLikeRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JpaCommentLikeRepository jpaCommentLikeRepository;
    @InjectMocks
    private CommentLikeServiceImpl commentLikeServiceImpl;



    @Test
    void 댓글좋아요_생성(){

        //given
        UserEntity user = new UserEntity("aaa@email.com","password","aaa","imageUrl");
        PostEntity post = new PostEntity(user,"title","content","imageUrl");
        CommentEntity comment = new CommentEntity(post,user,"content");
        CommentLikeEntity commentLike = new CommentLikeEntity(comment,user);

        given(jpaCommentLikeRepository.findByCommentIdAndUserId(any(), any())).willReturn(Optional.empty());
        //when
        boolean result = commentLikeServiceImpl.toggleCommentLike(11L,1L);
        //then
        assertThat(result).isTrue();

    }

    @Test
    void 댓글좋아요_취소(){
        //given
        UserEntity user = new UserEntity("aaa@email.com","password","aaa","imageUrl");
        PostEntity post = new PostEntity(user,"title","content","imageUrl");
        CommentEntity comment = new CommentEntity(post,user,"content");
        CommentLikeEntity commentLike = new CommentLikeEntity(comment,user);

        given(jpaCommentLikeRepository.findByCommentIdAndUserId(any(), any())).willReturn(Optional.of(commentLike));
        //when
        boolean result = commentLikeServiceImpl.toggleCommentLike(11L,1L);
        //then
        assertThat(result).isFalse();
    }


}
