package com.example.amumal_project.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public ResponseEntity<Map<String ,Object>> signup(@RequestBody User user) {


        User createdUser = userService.register(
                user.getEmail(),
                user.getPassword(),
                user.getNickname(),
                user.getProfileImageUrl()
        );
        Map<String, Object> response = new HashMap<>();
        response.put("message", "signup_success");
        response.put("data", createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
    public ResponseEntity<Map<String ,Object>> updateUserProfile(@PathVariable Long userId, @RequestBody Map<String, Object> request){
        String nickname = (String) request.get("nickname");
        String profileImageUrl = (String) request.get("profileImageUrl");

        User updatedUser = userService.updateUser(userId, nickname, profileImageUrl);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "update_success");
        response.put("data", updatedUser);
        return ResponseEntity.ok(response);

    }

    @PatchMapping("/{userId}/password")
    public ResponseEntity<Map<String ,Object>> updateUserPassword(@PathVariable Long userId, @RequestBody Map<String, String> request){
        String newPassword = request.get("newPassword");

        userService.updatePassword(userId, newPassword);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "update_success");
        response.put("data", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long userId){
        User deletedUser = userService.deleteUser(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "delete_success");
        response.put("data", null);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request){
        String email = request.get("email");
        String password = request.get("password");

        User user = userService.login(email, password);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "login_success");
        response.put("data", user);

        return ResponseEntity.ok(response);
    }


}
