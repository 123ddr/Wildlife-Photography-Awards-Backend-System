package com.wildlifebackend.wildlife.configuration;




import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


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
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(
                                "/api/auth/signup",
                                "/api/auth/login",
                                "/api/authz/signup_student",
                                "/api/authz/login_student",
                                "/submissions/create",
                                "/api/submissions/create",
                                "/auth/forgotpass",
                                "/api/photos/**"
                        )
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )

//                // Disable form login as it's unnecessary for stateless APIs
//                .formLogin(formLogin -> formLogin.disable());
//
//                // Enable basic authentication (can replace with JWT for better security)
//                .http.httpBasic(httpBasic -> httpBasic.disable());

                .httpBasic(withDefaults());

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
