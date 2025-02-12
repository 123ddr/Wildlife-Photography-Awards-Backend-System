package com.wildlifebackend.wildlife.configuration;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {


    private final OpenUserDetailsService openUserDetailsService;

    private final StudentDetailsService studentDetailsService;

    public SecurityConfig(OpenUserDetailsService openUserDetailsService, StudentDetailsService studentDetailsService) {
        this.openUserDetailsService = openUserDetailsService;
        this.studentDetailsService = studentDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for APIs (enable it with token-based CSRF protection in production)
                .csrf(csrf -> csrf.disable())

                // Configure endpoint-based authorization
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,
                                "/api/auth/signup",
                                "/api/auth/login",
                                "/api/authz/signup_student",
                                "/api/authz/login_student",
                                "/submissions/create",
                                "/api/submissions/create",
                                "/auth/forgotpass").permitAll() // Public endpoints
                        .anyRequest().authenticated() // Protect all other endpoints
                )

                // Disable form login as it's unnecessary for stateless APIs
                .formLogin(formLogin -> formLogin.disable());

                // Enable basic authentication (can replace with JWT for better security)
//                .httpBasic(httpBasic -> httpBasic.enable());

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(daoAuthenticationProvider())
                .build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(openUserDetailsService); // Custom user details service
        authProvider.setUserDetailsService(studentDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
