package com.example.amumal_project.api.user;

import com.example.amumal_project.api.auth.AuthService;
import com.example.amumal_project.api.auth.dto.AuthLoginResult;
import com.example.amumal_project.api.user.dto.UserRequest;
import com.example.amumal_project.api.user.repository.UserRepository;
import com.example.amumal_project.api.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private AuthService authService;

    private String accessToken = "accessToken";
    private String refreshToken = "refreshToken";

    @Test
    void 회원가입_성공() throws Exception {
        //given
        given(userService.register(any(),any(),any(),any())).willReturn(new User());
        String json = """
                          {
                                "email": "aaa@email.com",
                                "password": "password",
                                "nickname": "aaa",
                                "profile_image": "image/profile.png"
                          }
                          """;
        //when
        ResultActions resultActions = mockMvc.perform(post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("signup_success"));

        verify(userService).register(any(),any(),any(),any());

    }


    @Test
    void 로그인_성공() throws Exception {
        //given
        given(authService.login(any(),any())).willReturn(new AuthLoginResult(accessToken,refreshToken));
        String json = """
                          {
                                "email": "aaa@email.com",
                                "password": "password"
                          }
                          """;

        //when
        ResultActions resultActions = mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("login_success"));

        verify(authService).login(any(),any());

    }







}
