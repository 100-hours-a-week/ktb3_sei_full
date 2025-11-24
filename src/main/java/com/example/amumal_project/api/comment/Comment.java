package com.example.amumal_project.api.comment;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private Long userId;
    private Long postId;
    private String content;
    private int likeCount;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String nickname;

    public Comment() {}

    public Comment(Long id, Long userId, Long postId, String content,LocalDateTime created_at,int likeCount, String nickname) {
        this.userId = userId;
        this.postId = postId;
        this.id = id;
        this.content = content;
        this.likeCount = likeCount;
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
        this.nickname = nickname;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getUserId() {return userId;}
    public void setUserId(Long userId) {this.userId = userId;}

    public Long getPostId() {return postId;}
    public void setPostId(Long postId) {this.postId = postId;}

    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public LocalDateTime getCreated_at() {return created_at;}
    public void setCreated_at(LocalDateTime created_at) {this.created_at = created_at;}

    public LocalDateTime getUpdated_at() {return updated_at;}
    public void setUpdated_at(LocalDateTime updated_at) {this.updated_at = updated_at;}

    public int getLikeCount() {return likeCount;}
    public void setLikeCount(int likeCount) {this.likeCount = likeCount;}

    public String getNickname() {return nickname;}
    public void setNickname(String nickname) {this.nickname = nickname;}
}
