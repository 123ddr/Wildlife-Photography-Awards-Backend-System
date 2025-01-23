package com.wildlifebackend.wildlife.service.serviceImpl;


import com.wildlifebackend.wildlife.entitiy.OpenUser;
import com.wildlifebackend.wildlife.repository.OpenUserRepository;
import com.wildlifebackend.wildlife.service.OpenUserService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class OpenUserServiceImpl implements OpenUserService {


    private final OpenUserRepository openuserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // Constructor
    public OpenUserServiceImpl(OpenUserRepository openuserRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.openuserRepository = openuserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public void registerUser(OpenUser openuser) {
        if (openuserRepository.findByEmail(openuser.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        if (openuserRepository.findByNic(openuser.getNic()).isPresent()) {
            throw new IllegalArgumentException("NIC is already in use");
        }

        openuser.setPassword(passwordEncoder.encode(openuser.getPassword())); // Hash password
        openuserRepository.save(openuser);
    }

    public OpenUser loginUser(String email, String password) {
        OpenUser openuser = openuserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(password, openuser.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Authentication logic using Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return openuser;
    }
}
