package com.example.amumal_project.api.auth;

import com.example.amumal_project.api.auth.dto.AuthLoginResult;
import com.example.amumal_project.api.auth.dto.AuthReissueResult;
import com.example.amumal_project.api.user.User;
import com.example.amumal_project.api.user.dto.CustomUserinfoDto;
import com.example.amumal_project.api.user.repository.UserRepository;
import com.example.amumal_project.common.exception.UnauthorizedException;
import com.example.amumal_project.entity.RefreshTokenEntity;
import com.example.amumal_project.security.JwtTokenProvider;
import com.example.amumal_project.security.details.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public AuthLoginResult login(String email, String password){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        var authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        CustomUserinfoDto info = new CustomUserinfoDto(
                userDetails.getUserId(),
                userDetails.getEmail(),
                null
        );
        String accessToken = jwtTokenProvider.createAccessToken(info);
        String refreshToken = jwtTokenProvider.createRefreshToken(info);

        refreshTokenRepository.save(
                new RefreshTokenEntity(
                        userDetails.getUserId(),
                        refreshToken,
                        jwtTokenProvider.getRefreshTokenExpireTime()
                )
        );

        return new AuthLoginResult(accessToken, refreshToken);

    }

    public AuthReissueResult reissue(String refreshToken){
        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new UnauthorizedException("리프레쉬 토큰이 유효하지 않음"));

        if(!jwtTokenProvider.isValid(refreshToken)){
            throw new UnauthorizedException("리프레쉬 토큰 만료");
        }
        User user = userRepository.findById(tokenEntity.getUserId())
                .orElseThrow(()-> new UnauthorizedException("사용자를 찾을 수 없음"));

        CustomUserinfoDto dto = new CustomUserinfoDto(
                user.getId(),
                user.getEmail(),
                null
        );

        String newAccessToken = jwtTokenProvider.createAccessToken(dto);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(dto);

        tokenEntity.updateRefreshToken(newRefreshToken);
        refreshTokenRepository.save(tokenEntity);

        return new AuthReissueResult(newAccessToken, newRefreshToken);
    }

}
