package com.example.amumal_project.api.post;

import com.example.amumal_project.api.auth.dto.AuthLoginResult;
import com.example.amumal_project.api.comment.Comment;
import com.example.amumal_project.api.comment.dto.CommentDto;
import com.example.amumal_project.api.comment.service.CommentService;
import com.example.amumal_project.api.like.service.PostLikeService;
import com.example.amumal_project.api.post.dto.PostDto;
import com.example.amumal_project.api.post.service.PostService;
import com.example.amumal_project.api.user.User;
import com.example.amumal_project.api.user.UserController;
import com.example.amumal_project.api.user.service.UserService;
import com.example.amumal_project.security.details.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.Authentication;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PostService postService;
    @MockBean
    UserService userService;
    @MockBean
    CommentService commentService;
    @MockBean
    PostLikeService postLikeService;

    @Test
    void 게시글작성_성공() throws Exception {
        //given
        CustomUserDetails userDetails = new CustomUserDetails(1L,"aaa@email.com","password");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = new User(1L,"aaa@email.com","password","aaa","images/profile.jpg");
        given(userService.getUserById(1L)).willReturn(user);
        Post post = new Post(101L,2L,"tile","content","/images/post.png",0,0,0,"author_nickname",
                LocalDateTime.of(2025, 12, 4, 15, 23, 10),LocalDateTime.of(2025, 12, 4, 15, 23, 10));
        given(postService.createPost(any(),any(),any(),any())).willReturn(post);
        String json = """
                          {
                               "title": "title",
                               "content": "content",
                               "image_url":"images/post.jpg"
                          }
                          """;

        //when
        ResultActions resultActions = mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()));

        //then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("create_success"));

        verify(postService).createPost(any(),any(),any(),any());
    }

    @Test
    void 게시글상세조회_성공() throws Exception {
        //given
        CustomUserDetails userDetails = new CustomUserDetails(1L,"aaa@email.com","password");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = new User(1L,"aaa@email.com","password","aaa","images/profile.jpg");
        given(userService.getUserById(1L)).willReturn(user);

        Post post = new Post(101L,2L,"tile","content","/images/post.png",1,0,0,"author_nickname",
                LocalDateTime.of(2025, 12, 4, 15, 23, 10),LocalDateTime.of(2025, 12, 4, 15, 23, 10));
        given(postService.increaseViewCount(any())).willReturn(post);
        PostDto postDto = PostDto.toPostDto(post);

        Comment comment = new Comment(11L,101L,1L,"content",LocalDateTime.of(2025, 12, 4, 15, 23, 10),0,"aaa","images/profile.jpg");
        List<Comment> comments = List.of(comment);
        given(commentService.getCommentsByPostId(any())).willReturn(comments);

        //when
        ResultActions resultActions = mockMvc.perform(get("/posts/{postId}",101L)
                .with(csrf()));

        //then
        verify(userService).getUserById(1L);
        verify(postService).increaseViewCount(101L);
        verify(commentService).getCommentsByPostId(101L);


    }

}
