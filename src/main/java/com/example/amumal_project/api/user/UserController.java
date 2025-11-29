package com.example.amumal_project.api.user;

import com.example.amumal_project.api.auth.AuthService;
import com.example.amumal_project.api.auth.dto.AuthLoginResult;
import com.example.amumal_project.api.auth.dto.AuthReissueResult;
import com.example.amumal_project.api.user.dto.UserDto;
import com.example.amumal_project.api.user.dto.UserRequest;
import com.example.amumal_project.api.user.dto.UserResponse;
import com.example.amumal_project.common.CommonResponse;
import com.example.amumal_project.common.exception.ResourceNotFoundException;
import com.example.amumal_project.common.exception.UnauthorizedException;
import com.example.amumal_project.api.user.service.UserService;
import com.example.amumal_project.security.details.CustomUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(
        origins = "http://127.0.0.1:5500",
        allowCredentials = "true"
)
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private  final AuthService authService;

    public UserController(UserService userService,AuthService authService) {
        this.userService = userService;
        this.authService = authService;
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
    public ResponseEntity<CommonResponse> signup(@RequestBody UserRequest.SignupRequest user) {

        User createdUser = userService.register(
                user.getEmail(),
                user.getPassword(),
                user.getNickname(),
                user.getProfileImageUrl()
        );

        return ResponseEntity.ok()
                .body(new CommonResponse("signup_success"));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest.LoginRequest request,
                                   HttpServletResponse response) {

        AuthLoginResult tokens = authService.login(request.getEmail(), request.getPassword());

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", tokens.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(60 * 30)  // 30분
                .build();


        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(60 * 60 * 24 * 7)
                .build();


        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(Map.of(
                "message", "login_success"
        ));
    }

    //전체 회원 목록 조회
    @GetMapping
    public ResponseEntity<UserResponse.GetUsersResponse> getAllUsers(){
        List<User> users = userService.getAllUsers();

        List<UserDto> userDtos = users.stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .nickname(user.getNickname())
                        .profileImageUrl(user.getProfileImageUrl())
                        .build()).toList();
        return ResponseEntity.ok(UserResponse.GetUsersResponse.builder().users(userDtos).build());
    }

    //회원 정보 조회
    @GetMapping("/profile")
    public ResponseEntity<Map<String,Object>> getUserProfile(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        User loginUser = userService.getUserById(userDetails.getUserId());

        if(loginUser == null){
            throw new ResourceNotFoundException("사용자를 찾을 수 없습니다.");
        }
        UserDto userDto = UserDto.builder()
                .id(loginUser.getId())
                .email(loginUser.getEmail())
                .nickname(loginUser.getNickname())
                .profileImageUrl(loginUser.getProfileImageUrl())
                .build();


        Map<String,Object> response = new HashMap<>();
        response.put("message", "profile_success");
        response.put("data", userDto);

        return ResponseEntity.ok(response);
    }

    //회원 정보 수정
    @PatchMapping("/profile")
    public ResponseEntity<CommonResponse> updateUserProfile(@RequestBody UserRequest.ProfileEditRequest request){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        userService.updateUser(
                userDetails.getUserId(),
                request.getNickname(),
                request.getProfileImageUrl()
        );

        return ResponseEntity.ok(new CommonResponse("profile_update_success"));

    }

    //비밀번호 수정
    @PatchMapping("/password")
    public ResponseEntity<CommonResponse> updateUserPassword(@RequestBody UserRequest.PasswordResetRequest request){
        String newPassword = request.getNew_password();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        User loginUser = userService.getUserById(userDetails.getUserId());

        userService.updatePassword(loginUser.getId(), newPassword);


        return ResponseEntity.ok(new CommonResponse("update_success"));
    }

    //회원 탈퇴
    @DeleteMapping("/profile")
    public ResponseEntity<CommonResponse> deleteUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        User loginUser = userService.getUserById(userDetails.getUserId());

        User deletedUser = userService.deleteUser(loginUser.getId());

        return ResponseEntity.ok(new CommonResponse("delete_success"));
    }

    //로그아웃
    @DeleteMapping("/session")
    public ResponseEntity<CommonResponse> logout(HttpServletRequest request,
                                                 HttpServletResponse response){
        String refreshToken =null;
        if(request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if(refreshToken == null || refreshToken.isBlank()){
            throw new UnauthorizedException("리프레쉬 토큰이 없음");
        }

        userService.logout(refreshToken);

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken","")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());
        return ResponseEntity.ok(new CommonResponse("logout_success"));

    }


    //액세스 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<UserResponse.ReissueResponse> reissue(@RequestBody Map<String, String> body){
        String refreshToken = body.get("refresh_token");
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new UnauthorizedException("리프레쉬 토큰이 필요합니다.");
        }
        AuthReissueResult result = authService.reissue(refreshToken);
        UserResponse.ReissueResponse response = new UserResponse.ReissueResponse(
                "reissue_success",
                result.getAccessToken(),
                result.getRefreshToken()
        );
        return ResponseEntity.ok(response);
    }

}
