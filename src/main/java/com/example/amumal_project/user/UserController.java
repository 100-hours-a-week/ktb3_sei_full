package com.example.amumal_project.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(params="email")
    public ResponseEntity<String> checkEmail(@RequestParam String email){

        boolean exist = userService.checkEmailDuplicate(email);

        if(exist){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        return ResponseEntity.ok("Email is available");
    }

    @GetMapping(params = "nickname")
    public ResponseEntity<String> checkNickname(@RequestParam String nickname){

        boolean exist = userService.checkNicknameDuplicate(nickname);

        if(exist){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nickname already exists");
        }

        return ResponseEntity.ok("Nickname is available");
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {

        User createdUser = userService.register(
                user.getEmail(),
                user.getPassword(),
                user.getNickname(),
                user.getProfileImageUrl()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestParam String email){
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{userId}/profile")
    public ResponseEntity<User> updateUserProfile(@PathVariable Long userId, @RequestBody Map<String, Object> request){
        String nickname = (String) request.get("nickname");
        String profileImageUrl = (String) request.get("profileImageUrl");

        User updatedUser = userService.updateUser(userId, nickname, profileImageUrl);
        return ResponseEntity.ok(updatedUser);

    }

    @PatchMapping("/{userId}/password")
    public ResponseEntity<User> updateUserPassword(@PathVariable Long userId, @RequestBody Map<String, String> request){
        String newPassword = (String) request.get("newPassword");
        String confirmPassword = (String) request.get("confirmPassword");

        User updatedUser = userService.updatePassword(userId, newPassword, confirmPassword);
        return ResponseEntity.ok(updatedUser);
    }
}
