package com.wildlifebackend.wildlife.configuration;



import com.wildlifebackend.wildlife.entitiy.Student;

import com.wildlifebackend.wildlife.repository.StudentRepositary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class StudentDetailsService implements UserDetailsService {

    private final StudentRepositary studentRepositary;

    public StudentDetailsService(StudentRepositary studentRepositary) {
        this.studentRepositary = studentRepositary;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Student user = studentRepositary.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER") // Assign roles as per your requirement
                .build();
    }

}
