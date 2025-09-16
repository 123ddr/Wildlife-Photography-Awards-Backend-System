
package com.wildlifebackend.wildlife.service.serviceImpl;


import com.wildlifebackend.wildlife.entitiy.Student;
import com.wildlifebackend.wildlife.repository.StudentRepositary;
import com.wildlifebackend.wildlife.service.EmailService;
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
    private final EmailService emailService;

    // Constructor
    public StudentServiceImpl(StudentRepositary studentRepositary, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService) {
        this.studentRepositary = studentRepositary;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    @Transactional
    public void registerStudent(Student student) {
        if (studentRepositary.findBySchoolEmail(student.getSchoolEmail()).isPresent()) {
            throw new IllegalArgumentException("id is already in use");
        }

        if (studentRepositary.findBySchoolEmail(student.getPassword()).isPresent()) {
            throw new IllegalArgumentException("id is already in use");
        }

        // 3. Encode password before saving
        student.setPassword(passwordEncoder.encode(student.getPassword()));

        // 4. Set student as active (optional, if applicable)
        student.setIsActive(true);

        // 5. Get max current ID (or 0 if no students exist)
        Long maxId = studentRepositary.findMaxId().orElse(0L);

        // 6. Generate next student ID (e.g., "STU_0001")
        String studentId = String.format("STU_%04d", maxId + 1);

        // 7. Assign generated student ID
        student.setPhotographerId(studentId);

        // 8. Save student with generated ID
        Student savedStudent = studentRepositary.save(student);

        // 9. Send welcome email (optional)
        emailService.sendWelcomeEmail(
                savedStudent.getSchoolEmail(),
                savedStudent.getName(),
                savedStudent.getPhotographerId()
        );
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

}
