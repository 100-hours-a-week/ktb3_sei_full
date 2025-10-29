package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "post_likes")
public class CommentLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", unique = true )
    private CommentEntity comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private UserEntity user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public CommentLikeEntity() {}

    public CommentLikeEntity(CommentEntity comment, UserEntity user) {
        this.comment = comment;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }
}
