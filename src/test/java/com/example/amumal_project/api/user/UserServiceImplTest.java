package com.example.amumal_project.api.user;

import com.example.amumal_project.api.auth.RefreshTokenRepository;
import com.example.amumal_project.api.user.repository.UserRepository;
import com.example.amumal_project.api.user.service.UserServiceImpl;
import com.example.amumal_project.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private IllegalArgumentException illegalArgumentException;


    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void 회원가입_성공() {
        //given
        given(userRepository.findByEmail(Mockito.anyString())).willReturn(Optional.empty());
        given(userRepository.findByNickname(Mockito.anyString())).willReturn(Optional.empty());
        given(passwordEncoder.encode(Mockito.anyString())).willReturn("encodedPassword");

        User user = new User(1L,"aaa@email.com","encodedPassword", "aaa", "/images/profile.png");

        given(userRepository.save(any(User.class))).willReturn(user);
        //when
        User registeredUser = userService.register("aaa@email.com","password", "aaa", "/images/profile.png");
        //then
        assertThat(registeredUser.getNickname()).isEqualTo("aaa");
        assertThat(registeredUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(registeredUser.getEmail()).isEqualTo("aaa@email.com");
        assertThat(registeredUser.getProfileImageUrl()).isEqualTo("/images/profile.png");
    }


    @Test
    void 회원가입_이메일중복(){
        //given
        given(userRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(new User()));
        //when
        assertThatThrownBy(()->userService.register("aaa@email.com","password", "aaa", "/images/default_profile.png"))
                .isInstanceOf(IllegalArgumentException.class);
        //then
        verify(userRepository , never()).save(any(User.class));
    }

    @Test
    void 회원가입_닉네임중복(){
        //given
        given(userRepository.findByEmail(Mockito.anyString())).willReturn(Optional.empty());
        given(userRepository.findByNickname(Mockito.anyString())).willReturn(Optional.of(new User()));
        //when
        assertThatThrownBy(()->userService.register("aaa@email.com","password", "aaa", "/images/default_profile.png"))
                .isInstanceOf(IllegalArgumentException.class);;
        //then
        verify(userRepository , never()).save(any(User.class));
    }

    @Test
    void 회원가입_프로필이미지Null(){
        //given
        given(userRepository.findByEmail(Mockito.anyString())).willReturn(Optional.empty());
        given(userRepository.findByNickname(Mockito.anyString())).willReturn(Optional.empty());
        given(passwordEncoder.encode(Mockito.anyString())).willReturn("encodedPassword");

        User user = new User(1L,"aaa@email.com","encodedPassword", "aaa", "/images/default_profile.png");

        given(userRepository.save(any(User.class))).willReturn(user);
        //when
        User registeredUser = userService.register("aaa@email.com","password", "aaa", null);
        //then
        assertThat(registeredUser.getNickname()).isEqualTo("aaa");
        assertThat(registeredUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(registeredUser.getEmail()).isEqualTo("aaa@email.com");
        assertThat(registeredUser.getProfileImageUrl()).isEqualTo("/images/default_profile.png");

    }
    @Test
    void 이메일_중복확인_성공(){
        //given
        given(userRepository.findByEmail(Mockito.anyString())).willReturn(Optional.empty());
        //when
        boolean result = userService.checkEmailDuplicate("aaa@email.com");
        //then
        assertThat(result).isFalse();
    }

    @Test
    void 이메일_중복확인_실패(){
        //given
        User user = new User(1L,"aaa@email.com","encodedPassword", "aaa", "/images/default_profile.png" );
        given(userRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(user));
        //when
        boolean result = userService.checkEmailDuplicate("aaa@email.com");
        //then
        assertThat(result).isTrue();
    }

    @Test
    void 닉네임_중복확인_성공(){
        //given
        given(userRepository.findByNickname(Mockito.anyString())).willReturn(Optional.empty());
        //when
        boolean result = userService.checkNicknameDuplicate("aaa");
        //then
        assertThat(result).isFalse();
    }

    @Test
    void 닉네임_중복확인_실패(){
        //given
        User user = new User(1L,"aaa@email.com","encodedPassword", "aaa", "/images/default_profile.png" );
        given(userRepository.findByNickname(Mockito.anyString())).willReturn(Optional.of(user));
        //when
        boolean result = userService.checkNicknameDuplicate("aaa");
        //then
        assertThat(result).isTrue();
    }

    @Test
    void 유저목록조회(){
        //given
        User user = new User(1L,"aaa@email.com","encodedPassword", "aaa", "/images/default_profile.png" );
        given(userRepository.findAll()).willReturn(Arrays.asList(user));
        //when
        List<User> users = userService.getAllUsers();
        //then
        assertThat(users).hasSize(1);
    }

    @Test
    void 아이디로유저찾기_성공(){
        //given
        User user = new User(1L,"aaa@email.com","encodedPassword", "aaa", "/images/default_profile.png" );
        given(userRepository.findById(Mockito.any())).willReturn(Optional.of(user));
        //when
        User result = userService.getUserById(1L);
        //then
        assertThat(result).isEqualTo(user);
    }

    @Test
    void 아이디로유저찾기_실패(){
        //given
        given(userRepository.findById(Mockito.any())).willReturn(Optional.empty());
        //when,then
        assertThatThrownBy(()->userService.getUserById(1L))
                .isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void 이메일로유저찾기_성공(){
        //given
        User user = new User(1L,"aaa@email.com","encodedPassword", "aaa", "/images/default_profile.png" );
        given(userRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(new User()));
        //when
        User result = userService.getUserByEmail("aaa@email.com");
        //then
        assertThat(result).isNotEqualTo(user);
    }

    @Test
    void 이메일로유저찾기_실패(){
        //given
        given(userRepository.findByEmail(Mockito.anyString())).willReturn(Optional.empty());
        //when,then
        assertThatThrownBy(()->userService.getUserByEmail("aaa@email.com"))
                .isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void 유저정보_업데이트_성공(){
        //given
        User user = new User(1L,"aaa@email.com","encodedPassword", "bbb", "/images/default_profile.png" );
        given(userRepository.findById(Mockito.any())).willReturn(Optional.of(user));
        given(userRepository.update(Mockito.any(),Mockito.anyString(),Mockito.anyString())).willReturn(Optional.of(user));
        //when
        User result = userService.updateUser(1L,"bbb","/images/default_profile.png") ;
        //then
        assertThat(result).isEqualTo(user);
    }

    @Test
    void  유저정보_업데이트_사용자를찾을수없음(){
        //given
        given(userRepository.findById(Mockito.any())).willReturn(Optional.empty());
        //when
        assertThatThrownBy(()->userService.updateUser(1L,"bbb","/images/default_profile.png"))
                .isInstanceOf(ResourceNotFoundException.class);
        //then
        verify(userRepository, never()).update(1L,"bbb","/images/default_profile.png");

    }

    @Test
    void 유저정보_업데이트_닉네임중복(){
        //given
        User user1 = new User(1L,"aaa@email.com","encodedPassword", "aaa", "/images/default_profile.png" );
        User user2 = new User(2L,"bbb@email.com","encodedPassword", "bbb", "/images/default_profile.png" );

        given(userRepository.findById(Mockito.any())).willReturn(Optional.of(user1));
        given(userRepository.findByNickname(Mockito.anyString())).willReturn(Optional.of(user2));
        //when
        assertThatThrownBy(()->userService.updateUser(1L,"bbb","/images/default_profile.png"))
                .isInstanceOf(IllegalArgumentException.class);
        //then
        verify(userRepository, never()).update(1L,"bbb","/images/default_profile.png");

    }
    @Test
    void 유저정보_업데이트_프로필사진Null(){
        //given
        User user = new User(1L,"aaa@email.com","encodedPassword", "aaa", "/images/default_profile.png" );
        given(userRepository.findById(Mockito.any())).willReturn(Optional.of(user));
        given(userRepository.update(Mockito.any(),Mockito.anyString(),Mockito.anyString())).willReturn(Optional.of(user));
        //when
        User result = userService.updateUser(1L,"aaa",null) ;
        //then
        assertThat(result).isEqualTo(user);
    }

    @Test
    void 유저정보_업데이트_업데이트실패(){
        //given
        User user = new User(1L,"aaa@email.com","encodedPassword", "aaa", "/images/default_profile.png" );
        given(userRepository.findById(Mockito.any())).willReturn(Optional.of(user));
        given(userRepository.update(Mockito.any(),Mockito.anyString(),Mockito.anyString())).willReturn(Optional.of(user));
        given(userRepository.update(Mockito.any(),Mockito.anyString(),Mockito.anyString())).willReturn(Optional.empty());
        //when
        assertThatThrownBy(()->userService.updateUser(1L,"bbb","/images/new_profile.png"))
        .isInstanceOf(ResourceNotFoundException.class);
        //then
        verify(userRepository).update(anyLong(),anyString(),anyString());
    }

    @Test
    void 비밀번호_업데이트_성공(){
        //given
        User user = new User(1L,"aaa@email.com","newEncodedPassword", "aaa", "/images/default_profile.png" );
        given(userRepository.findById(Mockito.any())).willReturn(Optional.of(user));
        doNothing().when(userRepository).setPassword(Mockito.any(),Mockito.anyString());
        given(passwordEncoder.encode(Mockito.anyString())).willReturn("newEncodedPassword");
        //when
        User result = userService.updatePassword(1L,"newPassword");
        //then
        assertThat(result).isEqualTo(user);
    }

    @Test
    void 비밀번호_업데이트_사용자를찾을수없음(){
        //given
        given(userRepository.findById(Mockito.any())).willReturn(Optional.empty());

        //when
        assertThatThrownBy(()->userService.updatePassword(1L,"newPassword"))
                .isInstanceOf(ResourceNotFoundException.class);
        //then
        verify(userRepository, never()).setPassword(1L,"newPassword");
    }

    @Test
    void 비밀번호_업데이트_비밀번호Null(){
        //given
        given(userRepository.findById(Mockito.any())).willReturn(Optional.of(new User()));

        //when
        assertThatThrownBy(()->userService.updatePassword(1L,null))
                .isInstanceOf(IllegalArgumentException.class);
        //then
        verify(userRepository, never()).setPassword(1L,null);
    }

    @Test
    void 비밀번호_업데이트_비밀번호빈칸(){
        //given
        given(userRepository.findById(Mockito.any())).willReturn(Optional.of(new User()));

        //when
        assertThatThrownBy(()->userService.updatePassword(1L,""))
                .isInstanceOf(IllegalArgumentException.class);
        //then
        verify(userRepository, never()).setPassword(1L,"");
    }

    @Test
    void 회원탈퇴_성공(){
        //given
        User user = new User(1L,"aaa@email.com","newEncodedPassword", "aaa", "/images/default_profile.png" );
        given(userRepository.findById(Mockito.any())).willReturn(Optional.of(user));
        doNothing().when(userRepository).delete(Mockito.any());
        //when
        User result = userService.deleteUser(1L);
        //then
        assertThat(result).isEqualTo(user);
    }

    @Test
    void 회원탈퇴_사용자찾을수없음(){
        //given
        given(userRepository.findById(Mockito.any())).willReturn(Optional.empty());
        //when
        assertThatThrownBy(()-> userService.deleteUser(1L))
                .isInstanceOf(ResourceNotFoundException.class);
        //then
        verify(userRepository, never()).delete(1L);
    }

    @Test
    void 로그아웃_성공(){
        //given
        String token = UUID.randomUUID().toString();
        doNothing().when(refreshTokenRepository).deleteByRefreshToken(Mockito.anyString());
        //when
        userService.logout(token);
        //then
        verify(refreshTokenRepository).deleteByRefreshToken(token);
    }
}
