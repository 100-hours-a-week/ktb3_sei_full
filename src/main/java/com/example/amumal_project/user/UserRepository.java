package com.example.amumal_project.user;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {
    private final Map<Long, User> users = new LinkedHashMap<>();
    private long sequence = 0L;

    public User save(User user) {
        user.setId(++sequence);
        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
    public Optional<User> findByNickname(String nickname) {
        return users.values().stream()
                .filter(user -> user.getNickname().equals(nickname))
                .findFirst();
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public void delete(Long id) {
        users.remove(id);
    }

    public void clear() {
        users.clear();
        sequence = 0L;
    }

    public Optional<User> update(Long id, String nickname, String profileImageUrl) {
        User user = users.get(id);
        if(user == null) return Optional.empty();

        if(nickname != null  && !nickname.isBlank()) user.setNickname(nickname);
        if(profileImageUrl != null && !nickname.isBlank()) user.setProfileImageUrl(profileImageUrl);

        users.put(id, user);
        return Optional.of(user);
    }
}

