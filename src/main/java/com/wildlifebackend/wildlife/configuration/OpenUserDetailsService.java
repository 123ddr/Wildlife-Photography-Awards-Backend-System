package com.wildlifebackend.wildlife.configuration;


import com.wildlifebackend.wildlife.entitiy.OpenUser;
import com.wildlifebackend.wildlife.repository.OpenUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OpenUserDetailsService implements UserDetailsService {

    private final OpenUserRepository openUserRepository;

    public OpenUserDetailsService(OpenUserRepository openUserRepository) {
        this.openUserRepository = openUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        OpenUser user = openUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER") // Assign roles as per your requirement
                .build();
    }


}
