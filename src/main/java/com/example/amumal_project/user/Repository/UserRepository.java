package com.example.amumal_project.user.Repository;

import com.example.amumal_project.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    List<User> findAll();
    void delete(Long id);
    Optional<User> update(Long id, String nickname, String profileImageUrl);
}
