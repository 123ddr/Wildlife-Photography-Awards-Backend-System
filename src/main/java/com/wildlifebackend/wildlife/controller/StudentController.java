package com.wildlifebackend.wildlife.controller;

import com.wildlifebackend.wildlife.configuration.JwtConfig;
import com.wildlifebackend.wildlife.dto.response.APIResponse;
import com.wildlifebackend.wildlife.dto.response.TokenResponse;
import com.wildlifebackend.wildlife.entitiy.OpenUser;
import com.wildlifebackend.wildlife.entitiy.Student;
import com.wildlifebackend.wildlife.repository.StudentRepositary;
import com.wildlifebackend.wildlife.service.OpenUserService;
import com.wildlifebackend.wildlife.service.StudentService;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("api/authz")
public class StudentController {


    private final StudentService studentService;
    private final JwtConfig jwtConfig;

    @Autowired
    public StudentController(StudentService studentService, JwtConfig jwtConfig) {
        this.studentService = studentService;
        this.jwtConfig = jwtConfig;
    }



    @PostMapping("/signup_student")
    public ResponseEntity<APIResponse<String>> signUp(@Valid @RequestBody Student student) {
        try {
            studentService.registerStudent(student);
            return ResponseEntity.ok(APIResponse.success("User registered successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(APIResponse.error(e.getMessage()));
        }
    }


    @PostMapping("/login_student")
    public ResponseEntity<TokenResponse> login(@RequestParam String email, @RequestParam String password) {
        try {
            Student loginStudent = studentService.loginStudent(email, password);

            if (loginStudent != null) {
                String token = Jwts.builder()
                        .setSubject(email)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                        .signWith(jwtConfig.getSecretKey())
                        .compact();
                TokenResponse response = TokenResponse.builder()
                        .token(token)
                        .expireIn(3600000)
                        .build();


                return ResponseEntity.ok(response);
            } else {
                TokenResponse response = TokenResponse.builder()
                        .status(String.valueOf(HttpStatus.UNAUTHORIZED))
                        .message("Invalid email or password")
                        .build();
                return ResponseEntity.ok(response);
            }
        } catch (IllegalArgumentException e) {
            TokenResponse response = TokenResponse.builder()
                    .status(String.valueOf(HttpStatus.BAD_REQUEST))
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.ok(response);
        }

    }

}
