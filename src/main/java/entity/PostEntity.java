package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private UserEntity author;

    private String title;
    private String content;

    @Column(name = "image")
    private String postImageUrl;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "like_count")
    private Integer likeCount;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "comment_count")
    private Integer commentCount;

    @OneToMany(mappedBy = "post")
    private List<PostLikeEntity> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<CommentEntity> comments = new ArrayList<>();

    public PostEntity() {}

    public PostEntity(UserEntity author, String title, String content,String postImageUrl){
        this.author = author;
        this.title = title;
        this.content = content;
        this.postImageUrl = postImageUrl;
        this.isDeleted = false;
        this.deletedAt = null;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.likeCount = 0;
        this.viewCount = 0;
        this.commentCount = 0;
    }
}
