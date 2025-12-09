package com.example.amumal_project.security;

import com.example.amumal_project.api.user.dto.CustomUserinfoDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;


import static org.assertj.core.api.Assertions.*;

public class JwtTokenProviderTest {
    JwtTokenProvider jwtTokenProvider;
    String secretKey;
    long accessTokenTime = 1800000;
    long refreshTokenTime = 1209600000;
    @BeforeEach
    public void setUp() {
        secretKey = "dVFGUzA1bFpLSE9EYURGR0VVS3hSMTJhSW5mdGJrd2libzV5YUNSTUhvSA==";
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
       jwtTokenProvider = new JwtTokenProvider(secretKey,accessTokenTime,refreshTokenTime);
    }

    @Test
    void 액세스토큰생성_성공(){
        //given
        CustomUserinfoDto user = new CustomUserinfoDto(1L,"aaa@email.com","password");
        //when
        String accessToken = jwtTokenProvider.createAccessToken(user);
        //then
        assertThat(accessToken).isNotNull();
        assertThat(jwtTokenProvider.isValid(accessToken)).isTrue();
        assertThat(jwtTokenProvider.getUserId(accessToken)).isEqualTo(user.getId());
    }

    @Test
    void 리프레쉬토큰생성_성공(){
        //given
        CustomUserinfoDto user = new CustomUserinfoDto(1L,"aaa@email.com","password");
        //when
        String refreshToken= jwtTokenProvider.createRefreshToken(user);
        //then
        assertThat(refreshToken).isNotNull();
        assertThat(jwtTokenProvider.isValid(refreshToken)).isTrue();
        assertThat(jwtTokenProvider.getUserId(refreshToken)).isEqualTo(user.getId());

    }

    @Test
    void 토큰에서_아이디추출_성공(){
        //given
        CustomUserinfoDto user = new CustomUserinfoDto(1L,"aaa@email.com","password");
        String token = jwtTokenProvider.createAccessToken(user);
        //when
        long userId = jwtTokenProvider.getUserId(token);
        //then
        assertThat(userId).isEqualTo(user.getId());
    }

    @Test
    void 리프레쉬토큰_만료시간조회_성공(){
        //given

        //when
        long refreshTokenTime = jwtTokenProvider.getRefreshTokenExpireTime();
        //then
        assertThat(refreshTokenTime).isEqualTo(refreshTokenTime);
    }

    @Test
    void Claims추출_성공(){
        //given
        CustomUserinfoDto user = new CustomUserinfoDto(1L,"aaa@email.com","password");
        String accessToken = jwtTokenProvider.createAccessToken(user);
        //when
        Claims claim = jwtTokenProvider.parseClaims(accessToken);
        //then
        assertThat(claim).isNotNull();
        assertThat(claim).isInstanceOf(Map.class);
        assertThat((claim).get("userId")).isEqualTo(1);
    }

    @Test
    void 만료된토큰_Claims추출() throws InterruptedException{
        //given
        JwtTokenProvider shortJwtTokenProvider = new JwtTokenProvider(secretKey, 1, 1);
        CustomUserinfoDto user = new CustomUserinfoDto(1L,"aaa@email.com","password");
        String accessToken = shortJwtTokenProvider.createAccessToken(user);
        Thread.sleep(1500);
        //when
        Claims claim = jwtTokenProvider.parseClaims(accessToken);
        //then
        assertThat(claim).isNotNull();
        assertThat(claim).isInstanceOf(Map.class);
        assertThat((claim).get("userId")).isEqualTo(1);

    }

    @Test
    void 유효성검사_성공(){
        //given
        CustomUserinfoDto user = new CustomUserinfoDto(1L,"aaa@email.com","password");
        String accessToken = jwtTokenProvider.createAccessToken(user);
        //when
        boolean result = jwtTokenProvider.isValid(accessToken);
        //then
        assertThat(result).isTrue();

    }

    @Test
    void 유효성검사_형식이유효하지않음(){
        CustomUserinfoDto user = new CustomUserinfoDto(1L,"aaa@email.com","password");
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String invalidToken = accessToken + ".xx";
        //when
        boolean result = jwtTokenProvider.isValid(invalidToken);
        //then
        assertThat(result).isFalse();
    }


    @Test
    void 유효성검사_만료토큰() throws InterruptedException{
        //given
        JwtTokenProvider shortJwtTokenProvider = new JwtTokenProvider(secretKey, 1, 1);
        CustomUserinfoDto user = new CustomUserinfoDto(1L,"aaa@email.com","password");
        String accessToken = shortJwtTokenProvider.createAccessToken(user);
        Thread.sleep(1500);
        //when
        boolean result = jwtTokenProvider.isValid(accessToken);
        //then
        assertThat(result).isFalse();
    }
    @Test
    void 유효성검사_지원되지않는토큰(){
        //given
        CustomUserinfoDto user = new CustomUserinfoDto(1L,"aaa@email.com","password");
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String[] parts = accessToken.split("\\.");

        String header = new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
        header = header.replace("HS256","RS256");
        String invalidHeader = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(header.getBytes(StandardCharsets.UTF_8));

        String unsupportedToken = invalidHeader +"."+ parts[1]+"."+ parts[2];
        //when
        boolean result = jwtTokenProvider.isValid(unsupportedToken);
        //then
        assertThat(result).isFalse();
    }


    @Test
    void 유효성검사_Claims없음(){
        //given
        CustomUserinfoDto user = new CustomUserinfoDto(1L,"aaa@email.com","password");
        String accessToken = null;
        //when
        boolean result = jwtTokenProvider.isValid(accessToken);
        //then
        assertThat(result).isFalse();
    }

    @Test
    void 유효성검사_시그니처손상(){
        //given
        CustomUserinfoDto user = new CustomUserinfoDto(1L,"aaa@email.com","password");
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String invalidToken = accessToken.substring(0,accessToken.length()-2)+"xx";
        //when
        boolean result = jwtTokenProvider.isValid(invalidToken);
        //then
        assertThat(result).isFalse();
    }

}
