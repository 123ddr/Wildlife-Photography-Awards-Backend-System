package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.entitiy.Judge;
import com.wildlifebackend.wildlife.repository.JudgeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JudgeServiceImpl implements JudgeService {

    private final JudgeRepository judgeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public JudgeServiceImpl(JudgeRepository judgeRepository,
                            PasswordEncoder passwordEncoder,
                            AuthenticationManager authenticationManager) {
        this.judgeRepository = judgeRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    @Override
    public void registerJudge(Judge judge) {
        if (judgeRepository.findByEmail(judge.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        judge.setPassword(passwordEncoder.encode(judge.getPassword()));
        judge.setIsActive(true);

        judgeRepository.save(judge);
    }

    @Override
    public Judge loginJudge(String email, String password) {
        Judge judge = judgeRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Judge not found"));

        if (!passwordEncoder.matches(password, judge.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return judge;
    }
}
