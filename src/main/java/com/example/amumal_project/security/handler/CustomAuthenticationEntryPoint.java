package com.example.amumal_project.security.handler;

import com.example.amumal_project.common.exception.AccessDeniedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j(topic = "UNAUTHORIZED_EXCEPTION_HANDLER")
@AllArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException, ServletException{
        String exceptionMessage = (String) request.getAttribute("exception");
        Map<String, Object> body = new HashMap<>();
        body.put("status", 401);
        if(exceptionMessage != null){
            body.put("message", exceptionMessage);
        }else{
            body.put("message", "인증 실패, 토큰이 없거나 유효하지 않음");
        }

        body.put("timestamp", LocalDateTime.now().toString());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        String json = objectMapper.writeValueAsString(body);
        response.getWriter().write(json);

    }
}
