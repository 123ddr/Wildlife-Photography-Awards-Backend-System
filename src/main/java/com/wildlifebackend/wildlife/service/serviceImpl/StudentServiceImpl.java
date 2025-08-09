
package com.wildlifebackend.wildlife.service.serviceImpl;


import com.wildlifebackend.wildlife.entitiy.Student;
import com.wildlifebackend.wildlife.repository.StudentRepositary;
import com.wildlifebackend.wildlife.service.StudentService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class StudentServiceImpl implements StudentService {


    private final StudentRepositary studentRepositary;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // Constructor
    public StudentServiceImpl(StudentRepositary studentRepositary, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.studentRepositary = studentRepositary;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public void registerStudent(Student student) {
        if (studentRepositary.findBySchoolEmail(student.getSchoolEmail()).isPresent()) {
            throw new IllegalArgumentException("id is already in use");
        }

        if (studentRepositary.findBySchoolEmail(student.getPassword()).isPresent()) {
            throw new IllegalArgumentException("id is already in use");
        }

        student.setPassword(passwordEncoder.encode(student.getPassword())); // Hash password
        studentRepositary.save(student);
    }

    public Student loginStudent(String email, String password) {
        Student student = studentRepositary.findBySchoolEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(password, student.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Authentication logic using Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return student;
    }

    //connect the StudentPhotoController class
    public Student getAuthenticatedStudent(Authentication authentication) {
        return null;
    }
}
