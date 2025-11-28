package com.example.amumal_project.security.details;

import com.example.amumal_project.api.user.User;
import com.example.amumal_project.api.user.repository.AdapterUserRepository;
import com.example.amumal_project.api.user.repository.JpaUserRepository;
import com.example.amumal_project.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AdapterUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("로그인 실패: 존재하지 않은 이메일 입니다. {}", email);
                    return new UsernameNotFoundException("존재하지 않는 사용자 입니다.");
                });
        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword()
        );

    }
}
