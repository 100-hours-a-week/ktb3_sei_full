package com.example.amumal_project.api.auth;

import com.example.amumal_project.api.user.dto.CustomUserinfoDto;
import com.example.amumal_project.security.JwtTokenProvider;
import com.example.amumal_project.security.details.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(String email, String password){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        var authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return jwtTokenProvider.createAccessToken(
                new CustomUserinfoDto(
                        userDetails.getUserId(),
                        userDetails.getEmail(),
                        null
                )
        );
    }

}
