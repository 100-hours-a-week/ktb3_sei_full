package com.example.amumal_project.security.filter;

import com.example.amumal_project.security.JwtTokenProvider;
import com.example.amumal_project.security.details.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private static final String[] EXCLUDE_URLS = {
            "/users/login",
            "/users/signup",
            "/users/reissue"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        for (String url : EXCLUDE_URLS) {
            if (path.equals(url)) {
                return true;
            }
        }
        return false;
    }
    //JWT 검증필터
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException{
                                    String token = resolveToken(request);

        if(token != null && jwtTokenProvider.isValid(token)){
                                        Long userId = jwtTokenProvider.getUserId(token);
                                        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId.toString());

                                        var authorities = userDetails.getAuthorities();
                                        if (authorities == null || authorities.isEmpty()) {
                                            authorities = List.of(() -> "ROLE_USER");
                                        }

                                        UsernamePasswordAuthenticationToken authentication =
                                                new UsernamePasswordAuthenticationToken(
                                                        userDetails, null, userDetails.getAuthorities());
                                        SecurityContextHolder.getContext().setAuthentication(authentication);
                                    }
                                    filterChain.doFilter(request, response);


    }

    //쿠키에서 액세스 토큰 꺼내기
    private String resolveToken(HttpServletRequest request) {
        String cookieHeader = request.getHeader("Cookie");
        if(cookieHeader == null) return null;

        for(String cookie : cookieHeader.split(";")) {
            cookie = cookie.trim();
            if(cookie.startsWith("accessToken=")) {
                return cookie.substring("accessToken=".length());
            }
        }
        return null;

    }

}
