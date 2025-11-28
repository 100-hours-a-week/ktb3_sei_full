package com.example.amumal_project.api.user.service;

import com.example.amumal_project.common.exception.ResourceNotFoundException;
import com.example.amumal_project.api.user.User;
import com.example.amumal_project.api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Transactional
    public User register(String email, String password, String nickname, String profileImageUrl) {

        if(checkEmailDuplicate(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다!");
        }
        if(checkNicknameDuplicate(nickname)) {
            throw new IllegalArgumentException("이미 존재하는 닉네임 입니다!");
        }
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(null,email, encodedPassword, nickname, profileImageUrl);

        if(user.getProfileImageUrl() == null || user.getProfileImageUrl().isBlank()){
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
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다!"));
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다!"));
    }
    @Transactional
    public User updateUser(Long id, String nickname, String profileImageUrl) {
        if(nickname !=null) {
            User currentUser = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다!"));

            if (!nickname.equals(currentUser.getNickname()) && checkNicknameDuplicate(nickname)) {
                throw new IllegalArgumentException("이미 존재하는 닉네임 입니다!");
            }
        }

        if (profileImageUrl == null || profileImageUrl.isBlank()) {
            profileImageUrl = "/images/default_profile.png";
        }

        return userRepository.update(id, nickname, profileImageUrl)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다!"));

    }

    @Transactional
    public User updatePassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다!"));

        if(newPassword == null  || newPassword.isBlank()) {
            throw new IllegalArgumentException("새 비밀번호를 입력해주세요!");
        }

        userRepository.setPassword(user.getId(), newPassword);
        return user;
    }
    @Transactional
    public User deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다!"));
        userRepository.delete(id);

        return user;
    }





}