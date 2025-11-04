package com.example.amumal_project.api.user.repository;

import com.example.amumal_project.api.user.User;
import com.example.amumal_project.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class AdapterUserRepository implements UserRepository {
    private final JpaUserRepository jpaUserRepository;


    public AdapterUserRepository(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user){
        UserEntity userEntity = new UserEntity(user.getEmail(), user.getPassword(), user.getNickname(), user.getProfileImageUrl());
        UserEntity savedUser = jpaUserRepository.save(userEntity);
        return new User(savedUser.getId(), savedUser.getEmail(), savedUser.getPassword(), savedUser.getNickname(), savedUser.getProfileImageUrl());
    };

    @Override
    public Optional<User> findById(Long id){
        return jpaUserRepository.findById(id)
                .map(e -> new User(e.getId(), e.getEmail(), e.getPassword(), e.getNickname(), e.getProfileImageUrl()));
    };

    @Override
    public Optional<User> findByEmail(String email){
        return jpaUserRepository.findByEmail(email)
                .map(e-> new User(e.getId(), e.getEmail(), e.getPassword(), e.getNickname(), e.getProfileImageUrl()));
    };

    @Override
    public Optional<User> findByNickname(String nickname){
        return jpaUserRepository.findByNickname(nickname)
                .map(e-> new User(e.getId(), e.getEmail(), e.getPassword(), e.getNickname(), e.getProfileImageUrl()));
    };

    @Override
    public List<User> findAll(){
        return jpaUserRepository.findAll().stream()
                .map(e -> new User(e.getId(), e.getEmail(), e.getPassword(), e.getNickname(), e.getProfileImageUrl()))
                .toList();
    };

    @Override
    public void delete(Long id){
        UserEntity userEntity = jpaUserRepository.findById(id).orElseThrow();
        userEntity.setDeleted(true);
        userEntity.setDeletedAt(LocalDateTime.now());
    };

    @Override
    public Optional<User> update(Long id, String nickname, String profileImageUrl){
        UserEntity userEntity = jpaUserRepository.findById(id).orElseThrow();
        userEntity.setNickname(nickname);
        userEntity.setProfileImageUrl(profileImageUrl);
        userEntity.setUpdatedAt(LocalDateTime.now());
        return Optional.of(new User(userEntity.getId(), userEntity.getEmail(), userEntity.getPassword(), userEntity.getNickname(), userEntity.getProfileImageUrl()));
    };

    public void setPassword(Long id, String newPassword){
        UserEntity userEntity = jpaUserRepository.findById(id).orElseThrow();
        userEntity.setPassword(newPassword);
        userEntity.setUpdatedAt(LocalDateTime.now());
    }

}
