package com.example.amumal_project.user;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String email, String password, String nickname,String profileImageUrl) {

        if(checkEmailDuplicate(email)) {
            throw new IllegalArgumentException("Email already exists!");
        }
        if(checkNicknameDuplicate(nickname)) {
            throw new IllegalArgumentException("Nickname already exists!");
        }

        User user = new User(null, email, password, nickname, profileImageUrl);

        if(user.getProfileImageUrl() == null){
            user.setProfileImageUrl("/images/default_profile.png");
        }
        return userRepository.save(user);
    }

    public boolean checkEmailDuplicate(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean checkNicknameDuplicate(String nickname){
        return userRepository.findByNickname(nickname).isPresent();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    public User updateUser(Long id, String nickname, String profileImageUrl) {
        if(nickname !=null && checkNicknameDuplicate(nickname)) {
            throw new IllegalArgumentException("Nickname already exists!");
        }
        return userRepository.update(id, nickname, profileImageUrl)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

    }
}
