package com.wildlifebackend.wildlife.controller;


import com.wildlifebackend.wildlife.configuration.JwtConfig;
import com.wildlifebackend.wildlife.dto.response.APIResponse;
import com.wildlifebackend.wildlife.dto.response.TokenResponse;
import com.wildlifebackend.wildlife.entitiy.Judge;
import com.wildlifebackend.wildlife.service.JudgeService;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/judges")
public class JudgeController {

    private final JudgeService judgeService;
    private final JwtConfig jwtConfig;

    @Autowired
    public JudgeController(JudgeService judgeService, JwtConfig jwtConfig) {
        this.judgeService = judgeService;
        this.jwtConfig = jwtConfig;
    }

    @PostMapping("/signup")
    public ResponseEntity<APIResponse<String>> signUp(@Valid @RequestBody Judge judge) {
        try {
            judgeService.registerJudge(judge);
            return ResponseEntity.ok(APIResponse.success("Judge registered successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(APIResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestParam String email, @RequestParam String password) {
        try {
            judgeService.loginJudge(email, password);

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
