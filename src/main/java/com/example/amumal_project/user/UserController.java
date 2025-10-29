package com.example.amumal_project.user;

import com.example.amumal_project.common.exception.ResourceNotFoundException;
import com.example.amumal_project.common.exception.UnauthorizedException;
import com.example.amumal_project.user.service.UserService;
import com.example.amumal_project.user.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
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

    //이메일 중복 확인
    @GetMapping(params="email")
    public ResponseEntity<String> checkEmail(@RequestParam String email){

        boolean exist = userService.checkEmailDuplicate(email);

        if(exist){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일입니다.");
        }

        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }

    //닉네임 중복 확인
    @GetMapping(params = "nickname")
    public ResponseEntity<String> checkNickname(@RequestParam String nickname){

        boolean exist = userService.checkNicknameDuplicate(nickname);

        if(exist){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 닉네임입니다.");
        }

        return ResponseEntity.ok("사용 가능한 닉네임입니다.");
    }

    //회원가입
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

    //로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request, HttpSession session){
        String email = request.get("email");
        String password = request.get("password");

        User user = userService.login(email, password);

        session.setAttribute("loginUser", user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "login_success");
        response.put("data", user);

        return ResponseEntity.ok(response);
    }

    //전체 회원 목록 조회
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    //회원 정보 조회
    @GetMapping("/profile")
    public ResponseEntity<Map<String,Object>> getUserProfile(HttpSession session){
        User loginUser = (User) session.getAttribute("loginUser");

        if(loginUser == null){
            throw new ResourceNotFoundException("사용자를 찾을 수 없습니다.");
        }
        Map<String,Object> response = new HashMap<>();
        response.put("message", "profile_success");
        response.put("data", loginUser);

        return ResponseEntity.ok(response);
    }

    //회원 정보 수정
    @PatchMapping("/profile")
    public ResponseEntity<Map<String ,Object>> updateUserProfile(@RequestBody Map<String, Object> request, HttpSession session){

        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null){
            throw new UnauthorizedException("잘못된 접근입니다.");
        }

        String nickname = (String) request.get("nickname");
        String profileImageUrl = (String) request.get("profileImageUrl");

        User updatedUser = userService.updateUser(loginUser.getId(), nickname, profileImageUrl);
        session.setAttribute("loginUser", updatedUser);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "update_success");
        response.put("data", updatedUser);
        return ResponseEntity.ok(response);

    }

    //비밀번호 수정
    @PatchMapping("/password")
    public ResponseEntity<Map<String ,Object>> updateUserPassword(@RequestBody Map<String, String> request, HttpSession session){
        String newPassword = request.get("new_password");

        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null){
            throw new UnauthorizedException("잘못된 접근입니다.");
        }

        userService.updatePassword(loginUser.getId(), newPassword);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "update_success");
        response.put("data", null);
        return ResponseEntity.ok(response);
    }

    //회원 탈퇴
    @DeleteMapping("/profile")
    public ResponseEntity<Map<String, Object>> deleteUser(HttpSession session){
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null){
            throw new UnauthorizedException("잘못된 접근입니다.");
        }

        User deletedUser = userService.deleteUser(loginUser.getId());
        session.invalidate();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "delete_success");
        response.put("data", null);

        return ResponseEntity.ok(response);
    }

    //로그아웃
    @DeleteMapping("/session")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session){
        session.invalidate();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "logout_success");
        response.put("data", null);

        return ResponseEntity.ok(response);
    }




}
