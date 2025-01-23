package com.wildlifebackend.wildlife.controller;



import com.wildlifebackend.wildlife.configuration.JwtConfig;
import com.wildlifebackend.wildlife.entitiy.OpenUser;
import com.wildlifebackend.wildlife.service.OpenUserService;
import com.wildlifebackend.wildlife.dto.response.TokenResponse;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class OpenUserController {

    private final OpenUserService openUserService;
    private final JwtConfig jwtConfig;

    @Autowired
    public OpenUserController( OpenUserService openUserService, JwtConfig jwtConfig) {
        this.openUserService = openUserService;
        this.jwtConfig = jwtConfig;
    }



    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody OpenUser openuser) {
        try {
            openUserService.registerUser(openuser);
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }






    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestParam String email, @RequestParam String password) {
        try {
            OpenUser loginUser = openUserService.loginUser(email, password);

            if (loginUser != null) {
                // Generate JWT token
                String token = Jwts.builder()
                        .setSubject(email)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                        .signWith(jwtConfig.getSecretKey())
                        .compact();
                TokenResponse response = new TokenResponse();
                response.setToken(token);
                response.setExpireIn(3600000);
                response.setStatus(HttpStatus.OK.toString());


                return ResponseEntity.ok(response);
            } else {
                TokenResponse response = new TokenResponse();
                response.setStatus(String.valueOf(HttpStatus.UNAUTHORIZED));
                response.setMessage("Invalid email or password");


                return ResponseEntity.ok(response);
            }
        } catch (IllegalArgumentException e) {
            TokenResponse response = new TokenResponse();
            response.setStatus(String.valueOf(HttpStatus.BAD_REQUEST));
            response.setMessage(e.getMessage());
            return ResponseEntity.ok(response);
        }

    }

}

