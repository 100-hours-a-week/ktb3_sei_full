package com.example.amumal_project.security.filter;

import com.example.amumal_project.security.JwtTokenProvider;
import com.example.amumal_project.security.details.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    //JWT 검증필터
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException{
                                    String authorizationHeader = request.getHeader("Authorization");

                                    if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                                        String token = authorizationHeader.substring(7);

                                        if(jwtTokenProvider.isValid(token)) {
                                            Long userId = jwtTokenProvider.getUserId(token);

                                            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId.toString());

                                            if(userDetails != null) {
                                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                                        userDetails, null, userDetails.getAuthorities());

                                                SecurityContextHolder.getContext().setAuthentication(authentication);
                                            }

                                        }
                                    }
                                    filterChain.doFilter(request, response);
    }
}
