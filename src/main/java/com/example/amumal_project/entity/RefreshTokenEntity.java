package com.example.amumal_project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "refresh_tokens")
public class RefreshTokenEntity {
    @Id
    private Long userId;

    @Column(nullable = false, unique = true,length = 300)
    private String refreshToken;

    @Column(nullable = false)
    private Long expiration;

    public void updateRefreshToken(String newToken) {
        this.refreshToken = newToken;
    }
}
