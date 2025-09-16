package com.wildlifebackend.wildlife.service.serviceImpl;


import com.wildlifebackend.wildlife.entitiy.OpenUser;
import com.wildlifebackend.wildlife.repository.OpenUserRepository;
import com.wildlifebackend.wildlife.service.EmailService;
import com.wildlifebackend.wildlife.service.OpenUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final EmailService emailService;

    @Autowired
    public OpenUserServiceImpl(OpenUserRepository openuserRepository,
                         PasswordEncoder passwordEncoder,
                         AuthenticationManager authenticationManager,
                               EmailService emailService) {
        this.openuserRepository = openuserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }


    @Transactional
    public void registerUser(OpenUser openuser) {
        if (openuserRepository.findByEmail(openuser.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }
        if (openuserRepository.findByNic(openuser.getNic()).isPresent()) {
            throw new IllegalArgumentException("NIC is already in use");
        }

        openuser.setPassword(passwordEncoder.encode(openuser.getPassword()));
        openuser.setIsActive(true);

        // 1. Get max current id (or 0 if no users)
        Long maxId = openuserRepository.findMaxId().orElse(0L);

        // 2. Generate next ID number (maxId + 1)
        String photographerId = String.format("OpenUser_ID_%04d", maxId + 1);

        // 3. Set photographerId before saving
        openuser.setPhotographerId(photographerId);

        // 4. Save user with photographerId set
        OpenUser savedUser = openuserRepository.save(openuser);

        // 5. Send welcome email
        emailService.sendWelcomeEmail(
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getPhotographerId()
        );
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
