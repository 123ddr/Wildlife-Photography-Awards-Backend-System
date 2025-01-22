package com.wildlifebackend.wildlife.Configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Getter
@Configuration
public class JwtConfig {

    private final Key secretKey;

    public JwtConfig(@Value("${jwt.secret-key}") String secretKey) {
        // Decode the Base64 key and create a SecretKeySpec
        this.secretKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey), "HmacSHA256");
    }

}

