package com.example.amumal_project.security.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomAuthenticationEntryPointTest {
    @Mock
    ObjectMapper objectMapper;
    @InjectMocks
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Test
    void 인증실패처리_성공() throws IOException, ServletException {
        //given

        HttpServletRequest request = new MockHttpServletRequest("POST","/posts");
        HttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authenticationException = new AuthenticationException("Authentication Failed") {
        };
        request.setAttribute("exception", "Authentication Failed");
        String mockJsonResponse = "{\"status\":401, \"message\":\"Authentication Failed\", \"timestamp\":\"...\"}";
        given(objectMapper.writeValueAsString(any(Map.class))).willReturn(mockJsonResponse);
        //when
        customAuthenticationEntryPoint.commence(request, response, authenticationException);
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }

    @Test
    void 인증실패처리_예외메세지Null()  throws IOException, ServletException{
        //given

        HttpServletRequest request = new MockHttpServletRequest("POST","/posts");
        HttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authenticationException = new AuthenticationException("Authentication Failed") {
        };
        request.setAttribute("exception", null);
        String mockJsonResponse = "{\"status\":401, \"message\":\"Authentication Failed\", \"timestamp\":\"...\"}";
        given(objectMapper.writeValueAsString(any(Map.class))).willReturn(mockJsonResponse);
        //when
        customAuthenticationEntryPoint.commence(request, response, authenticationException);
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());



    }


}
