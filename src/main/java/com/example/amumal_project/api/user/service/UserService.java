package com.example.amumal_project.api.user.service;

import com.example.amumal_project.api.user.User;
import org.apache.commons.lang3.mutable.Mutable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User register(String email, String password, String nickname, String profileImageUrl);
    boolean checkEmailDuplicate(String email);
    boolean checkNicknameDuplicate(String nickname);
    List<User> getAllUsers();
    User getUserById(Long id);
    User getUserByEmail(String email);
    User updateUser(Long id, String nickname, String profileImageUrl);
    User updatePassword(Long id, String newPassword);
    User deleteUser(Long id);
    User login(String email, String password);
}
