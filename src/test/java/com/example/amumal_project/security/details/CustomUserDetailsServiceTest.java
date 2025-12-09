package com.example.amumal_project.security.details;

import com.example.amumal_project.api.user.User;
import com.example.amumal_project.api.user.repository.AdapterUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
    @Mock
    AdapterUserRepository adapterUserRepository;
    @InjectMocks
    CustomUserDetailsService customUserDetailsService;

    private User user;
    @Test
    void 유저로드_아이디(){
        //given
        String value = "1";
        User user = new User(1L,"aaa@email.com","password","aaa","profile.png");
        given(adapterUserRepository.findById(any())).willReturn(Optional.of(user));
        //when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(value);
        //then
        assertThat(userDetails.getUsername()).isEqualTo("aaa@email.com");
        assertThat(userDetails.getPassword()).isEqualTo("password");
    }
    @Test
    void 유저로드_존재하지않음_아이디(){
        //given
        String value = "2";
        given(adapterUserRepository.findById(any())).willReturn(Optional.empty());
        //when&then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(value))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void 유저로드_이메일(){
        //given
        String value = "aaa@email.com";
        User user = new User(1L,"aaa@email.com","password","aaa","profile.png");
        given(adapterUserRepository.findByEmail(any())).willReturn(Optional.of(user));
        //when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(value);
        //then
        assertThat(userDetails.getUsername()).isEqualTo("aaa@email.com");
        assertThat(userDetails.getPassword()).isEqualTo("password");
    }


    @Test
    void 유저로드_존재하지않음_이메일(){
        //given
        String value = "aaa@email.com";
        given(adapterUserRepository.findByEmail(any())).willReturn(Optional.empty());
        //when&then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(value))
                .isInstanceOf(UsernameNotFoundException.class);

    }
}
