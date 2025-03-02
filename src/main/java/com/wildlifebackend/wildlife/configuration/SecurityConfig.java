package com.wildlifebackend.wildlife.configuration;



import com.wildlifebackend.wildlife.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {



    private final OpenUserDetailsService openUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final StudentDetailsService studentDetailsService;

    public SecurityConfig(OpenUserDetailsService openUserDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter, StudentDetailsService studentDetailsService) {
        this.openUserDetailsService = openUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.studentDetailsService = studentDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs

//        To ensure CSRF is completely disabled for API requests, explicitly exclude it for all endpoints:
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,
                                "/api/auth/signup",
                                "/api/auth/login",
                                "/api/authz/signup_student",
                                "/api/authz/login_student",

                                "/submissions/create",
                                "/schoolsubmission/createSchoolSubmission",
                                "/api/photos/**",

                                "/api/open_submissions/create",
                                "/api/schoolsubmission/createSchoolSubmission",
                                "/auth/forgotpass",
                                "/api/studentphotosphotos/**"
                                                ).permitAll()
                        .anyRequest().authenticated()
                )

                // Properly add JwtAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .formLogin(formLogin -> formLogin.disable()); // Disable form login

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(openAuthProvider())
                .authenticationProvider(studentAuthProvider() )
                .build();
    }



    @Bean
    public DaoAuthenticationProvider openAuthProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(openUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public DaoAuthenticationProvider studentAuthProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(studentDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}
