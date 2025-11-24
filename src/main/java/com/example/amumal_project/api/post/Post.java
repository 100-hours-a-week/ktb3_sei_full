package com.example.amumal_project.api.post;

import java.time.LocalDateTime;

public class Post {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String ImageUrl;
    private int likeCount;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String nickname;

    public Post(Long id, Long userId, String title, String content,String imageUrl,int view_count, int like_count, String nickname, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.viewCount = view_count;
        this.likeCount = like_count;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.ImageUrl = imageUrl;
        this.nickname = nickname;
    }

    public Post() {}

    public String getImageUrl() {return ImageUrl;}
    public void setImageUrl(String imageUrl) {ImageUrl = imageUrl;}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getUserId() {return userId;}
    public void setUserId(Long userId) {this.userId = userId;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public LocalDateTime getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}

    public int getViewCount() {return viewCount;}
    public void setViewCount(int view_count) {this.viewCount = view_count;}

    public int getLikeCount() {return likeCount;}
    public void setLikeCount(int like_count) {this.likeCount = like_count;}

    public String getNickname() {return nickname;}
    public void setNickname(String nickname) {this.nickname = nickname;}
}
