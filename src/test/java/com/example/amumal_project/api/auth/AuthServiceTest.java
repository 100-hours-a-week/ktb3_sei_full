package com.example.amumal_project.api.auth;

import com.example.amumal_project.api.auth.dto.AuthLoginResult;
import com.example.amumal_project.api.auth.dto.AuthReissueResult;
import com.example.amumal_project.api.user.User;
import com.example.amumal_project.api.user.repository.UserRepository;
import com.example.amumal_project.common.exception.UnauthorizedException;
import com.example.amumal_project.entity.RefreshTokenEntity;
import com.example.amumal_project.security.JwtTokenProvider;
import com.example.amumal_project.security.details.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;



@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JwtTokenProvider jwtTokenProvider;
    @Mock
    RefreshTokenRepository refreshTokenRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    Authentication authentication;
    @InjectMocks
    AuthService authService;

    private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
            = new UsernamePasswordAuthenticationToken("aaa@email.com","password");
    private CustomUserDetails customUserDetails = new CustomUserDetails(1L,"aaa@email.com","password");
    private String accessToken = UUID.randomUUID().toString();
    private String refreshToken = UUID.randomUUID().toString();
    private RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(1L,refreshToken,(long)1209600000);
    private User user = new User(1L,"aaa@email.com","password","aaa","profileImageUrl");

    @Test
    void 로그인_성공(){
      //given

        AuthLoginResult authLoginResult = new AuthLoginResult(accessToken,refreshToken);

        given(authenticationManager.authenticate(any())).willReturn(authentication);
        given(authentication.getPrincipal()).willReturn(customUserDetails);
        given(jwtTokenProvider.createAccessToken(any())).willReturn(accessToken);
        given(jwtTokenProvider.createRefreshToken(any())).willReturn(refreshToken);
        given(refreshTokenRepository.save(any())).willReturn(refreshTokenEntity);

      //when
        AuthLoginResult result = authService.login("aaa@email.com","password");

      //then
        assertThat(result.getAccessToken()).isEqualTo(accessToken);
        assertThat(result.getRefreshToken()).isEqualTo(refreshToken);


    }

    @Test
    void 리프레쉬토큰_재발급_성공(){
        //given
        given(refreshTokenRepository.findByRefreshToken(any())).willReturn(Optional.of(refreshTokenEntity));
        given(jwtTokenProvider.isValid(any())).willReturn(true);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(jwtTokenProvider.createAccessToken(any())).willReturn(accessToken);
        given(jwtTokenProvider.createRefreshToken(any())).willReturn(refreshToken);
        given(refreshTokenRepository.save(any())).willReturn(refreshTokenEntity);
        AuthReissueResult authReissueResult = new AuthReissueResult(accessToken,refreshToken);
        //when
        AuthReissueResult result = authService.reissue(refreshToken);
        //then
        assertThat(result.getAccessToken()).isEqualTo(accessToken);
        assertThat(result.getRefreshToken()).isEqualTo(refreshToken);
    }

    @Test
    void 리프레쉬토큰_조회불가(){
        //given
        given(refreshTokenRepository.findByRefreshToken(any())).willReturn(Optional.empty());
        //when
        assertThatThrownBy(() -> authService.reissue(refreshToken))
        .isInstanceOf(UnauthorizedException.class);
        //then
        verify(authenticationManager, never()).authenticate(any());

    }

    @Test
    void 리프레쉬토큰_만료(){
        //given
        given(refreshTokenRepository.findByRefreshToken(any())).willReturn(Optional.of(refreshTokenEntity));
        given(jwtTokenProvider.isValid(any())).willReturn(false);
        //when
        assertThatThrownBy(() -> authService.reissue(refreshToken))
                .isInstanceOf(UnauthorizedException.class);
        //then
        verify(authenticationManager, never()).authenticate(any());

    }

    @Test
    void 리프레쉬토큰_유저조회불가(){
        //given
        given(refreshTokenRepository.findByRefreshToken(any())).willReturn(Optional.of(refreshTokenEntity));
        given(jwtTokenProvider.isValid(any())).willReturn(true);
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> authService.reissue(refreshToken))
                .isInstanceOf(UnauthorizedException.class);
        //then
        verify(authenticationManager, never()).authenticate(any());

    }


}
