package com.example.amumal_project.api.post.repository;

import com.example.amumal_project.api.post.Post;
import com.example.amumal_project.entity.PostEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface JpaPostRepository extends JpaRepository<PostEntity, Long> {
    @Query("SELECT p FROM PostEntity p JOIN FETCH p.author")
    List<PostEntity> findAllByAuthor();
    List<PostEntity> findByIsDeletedFalseOrderByIdDesc();
    Page<PostEntity> findByIsDeletedFalse(Pageable pageable);

}
