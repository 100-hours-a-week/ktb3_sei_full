package com.example.amumal_project.user;

import org.springframework.stereotype.Service;

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
}
