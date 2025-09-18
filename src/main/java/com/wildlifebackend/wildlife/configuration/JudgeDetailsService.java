package com.wildlifebackend.wildlife.configuration;



import com.wildlifebackend.wildlife.entitiy.Judge;
import com.wildlifebackend.wildlife.repository.JudgeRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class JudgeDetailsService implements UserDetailsService {

    private final JudgeRepository judgeRepository;

    public JudgeDetailsService(JudgeRepository judgeRepository) {
        this.judgeRepository = judgeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Judge judge = judgeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Judge not found with email: " + email));

        return User.builder()
                .username(judge.getEmail())
                .password(judge.getPassword())
                .roles("JUDGE")
                .disabled(!judge.getIsActive())
                .build();
    }
}

