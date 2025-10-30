package com.example.amumal_project.api.user.repository;

import com.example.amumal_project.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository  extends JpaRepository<UserEntity, Long> {

}
