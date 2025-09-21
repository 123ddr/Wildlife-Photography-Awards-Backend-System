package com.wildlifebackend.wildlife.controller;


import com.wildlifebackend.wildlife.configuration.JwtConfig;
import com.wildlifebackend.wildlife.dto.response.APIResponse;
import com.wildlifebackend.wildlife.dto.response.TokenResponse;
import com.wildlifebackend.wildlife.entitiy.Admin;
import com.wildlifebackend.wildlife.service.AdminServiceImpl;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminServiceImpl adminService;
    private final JwtConfig jwtConfig;

    @Autowired
    public AdminController(AdminServiceImpl adminService, JwtConfig jwtConfig) {
        this.adminService = adminService;
        this.jwtConfig = jwtConfig;
    }

    @PostMapping("/signup")
    public ResponseEntity<APIResponse<String>> signUp(@Valid @RequestBody Admin admin) {
        try {
            adminService.registerAdmin(admin);
            return ResponseEntity.ok(APIResponse.success("Admin registered successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(APIResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestParam String email, @RequestParam String password) {
        try {
            // Validate credentials (throws exception if invalid)
            adminService.loginAdmin(email, password);

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

        } catch (IllegalArgumentException e) {
            TokenResponse response = TokenResponse.builder()
                    .status(String.valueOf(HttpStatus.BAD_REQUEST))
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.ok(response);
        }
    }

}

