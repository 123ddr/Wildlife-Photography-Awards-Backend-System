package com.wildlifebackend.wildlife.configuration;


import com.wildlifebackend.wildlife.entitiy.Admin;
import com.wildlifebackend.wildlife.entitiy.OpenUser;
import com.wildlifebackend.wildlife.repository.AdminRepository;
import com.wildlifebackend.wildlife.repository.OpenUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public AdminDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin user = adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Map Admin entity to Spring Security User with ADMIN role
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("ADMIN") // must match your security config
                .build();
    }

}
