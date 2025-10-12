package com.example.amumal_project.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> signup(@RequestBody User user) {

        userService.register(
                user.getEmail(),
                user.getPassword(),
                user.getNickname(),
                user.getProfileImageUrl()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

}
