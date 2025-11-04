package com.example.amumal_project.api.post.repository;

import com.example.amumal_project.api.post.Post;
import com.example.amumal_project.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface JpaPostRepository extends JpaRepository<PostEntity, Long> {

}
