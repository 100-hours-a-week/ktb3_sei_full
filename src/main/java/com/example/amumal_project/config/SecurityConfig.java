package com.example.amumal_project.config;

import com.example.amumal_project.security.JwtTokenProvider;
import com.example.amumal_project.security.details.CustomUserDetailsService;
import com.example.amumal_project.security.filter.JwtAuthFilter;
import com.example.amumal_project.security.handler.CustomAuthenticationEntryPoint;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@AllArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    public final JwtTokenProvider jwtTokenProvider;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    private static final String[] AUTH_WHITELIST = {"/users/signup"
            ,"/users/login"
            ,"/css/**"
            ,"/js/**"
            ,"fronts/**"
            ,"/images/**"
            ,"/favicon.ico"
            ,"/swagger-ui/**"
            ,"/v3/api-docs/**"
            ,"/upload/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.cors(Customizer.withDefaults());

        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
        ));

        http.addFilterBefore(new JwtAuthFilter(customUserDetailsService, jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(
                authenticationEntryPoint));

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
        );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
