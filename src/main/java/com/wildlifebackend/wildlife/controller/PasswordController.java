package com.wildlifebackend.wildlife.controller;


import com.wildlifebackend.wildlife.PassGener.PasswordGenerater;
import com.wildlifebackend.wildlife.entitiy.OpenUser;
import com.wildlifebackend.wildlife.entitiy.Student;
import com.wildlifebackend.wildlife.repository.OpenUserRepository;
import com.wildlifebackend.wildlife.repository.StudentRepositary;
import com.wildlifebackend.wildlife.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class PasswordController {

    @Autowired
    private StudentRepositary studentRepositary;

    @Autowired
    private OpenUserRepository openUserRepository;

    @Autowired
    private EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/forgotpass")
    public ResponseEntity<String> forgotPass(@RequestParam String email) {
        Optional<Student> student = studentRepositary.findBySchoolEmail(email);
        Optional<OpenUser> openUser = openUserRepository.findByEmail(email);


        if (student.isPresent()) {
            return resetPassword(student.get(), student.get().getSchoolEmail());
        } else if (openUser.isPresent()) {
            return resetPassword(openUser.get(), openUser.get().getEmail());
        }

        return ResponseEntity.badRequest().body("Email not found");
    }

private ResponseEntity<String> resetPassword(Object user, String email){
    String newPassword = PasswordGenerater.generatePassword(8);
    String hashedPassword = passwordEncoder.encode(newPassword);

    if (user instanceof Student) {
        ((Student) user).setPassword(hashedPassword);
        studentRepositary.save((Student) user);
    } else if (user instanceof OpenUser) {
        ((OpenUser) user).setPassword(hashedPassword);
        openUserRepository.save((OpenUser) user);
    }

    // HTML email content
    String emailContent = """
                <p>Dear user,</p>
                <p>Your password has been reset successfully. Your new password is:</p>
                <h3 style="color: blue;">%s</h3>
                <p>Please log in and change your password immediately for security reasons.</p>
                <p>Best regards,<br><em>Wildlife</em></p>
                """.formatted(newPassword);

    emailService.sendEmail(email, "Password Reset", emailContent);

    return ResponseEntity.ok("New password has been sent to your email.");
}

}
