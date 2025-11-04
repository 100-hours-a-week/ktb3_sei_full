package com.example.amumal_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    private String nickname;

    @Column(name = "profile_image")
    private String profileImageUrl;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "author")
    List<PostEntity> posts = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    List<CommentEntity> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<PostLikeEntity> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<CommentLikeEntity> commentLikes = new ArrayList<>();


    public UserEntity() {}

    public UserEntity( String email, String password, String nickname, String profileImageUrl) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.isDeleted = false;
        this.deletedAt = null;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
