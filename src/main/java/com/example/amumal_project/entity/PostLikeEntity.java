package com.example.amumal_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "post_likes")
public class PostLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", unique = true)
    private PostEntity post;

    @Column(name = "created_at")
    private LocalDateTime createdAt;




    public PostLikeEntity(){}

    public PostLikeEntity(UserEntity user, PostEntity post){
        this.post = post;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }
}
