package com.example.amumal_project.security;

import com.example.amumal_project.api.user.dto.CustomUserinfoDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;
    private final long accessTokenExTime;
    private final long refreshTokenExTime;

    public JwtTokenProvider(
            @Value("${jwt.secret}") final String secretKey,
            @Value("${jwt.accessTokenExTime}") final long accessTokenExTime,
            @Value("${jwt.refreshTokenExTime}") final long refreshTokenExTime
    )
    {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExTime = accessTokenExTime;
        this.refreshTokenExTime = refreshTokenExTime;
    }

    //Access Token 생성
    public String createAccessToken(CustomUserinfoDto user){
        return createToken(user, accessTokenExTime);
    }
    //Refresh Token 생성
    public String createRefreshToken(CustomUserinfoDto user){
        return createToken(user, refreshTokenExTime);
    }
    //Jwt 생성
    private String createToken(CustomUserinfoDto user, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    //토큰에서 UserId 추출
    public Long getUserId(String token){
        return parseClaims(token).get("userId",Long.class);
    }

    public long getRefreshTokenExpireTime() {
        return refreshTokenExTime;
    }

    //Jwt Claims 추출
    public Claims parseClaims(String accessToken){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    public boolean isValid(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch(SecurityException | MalformedJwtException e){
            log.info("Invalid token", e);
        }catch(ExpiredJwtException e){
            log.info("Expired token", e);
        }catch(UnsupportedJwtException e){
            log.info("Unsupported token", e);
        }catch(IllegalArgumentException e){
            log.info("Jwt claims string is empty", e);
        }
        return false;
    }
}

