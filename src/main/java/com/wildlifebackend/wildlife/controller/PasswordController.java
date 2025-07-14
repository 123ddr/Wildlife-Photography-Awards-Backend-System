package com.wildlifebackend.wildlife.controller;


import com.wildlifebackend.wildlife.PassGener.PasswordGenerater;
import com.wildlifebackend.wildlife.entitiy.OpenUser;
import com.wildlifebackend.wildlife.entitiy.Student;
import com.wildlifebackend.wildlife.repository.OpenUserRepository;
import com.wildlifebackend.wildlife.repository.StudentRepositary;
import com.wildlifebackend.wildlife.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/forgotpass")
    public ResponseEntity<String> forgotPass(@RequestParam String email) {
        Optional<Student> student = studentRepositary.findBySchoolEmail(email);
        Optional<OpenUser> openUser = openUserRepository.findByEmail(email);


        if (student.isPresent()) {
            return resetPassword(student.get());
        } else if (openUser.isPresent()) {
            return resetPassword(openUser.get());

        }

        return ResponseEntity.badRequest().body("Email not found");
    }


private ResponseEntity<String> resetPassword(Object user){
    String newPassword = PasswordGenerater.generatePassword(5);

    if(user instanceof Student){
        Student student=(Student) user;
        student.setPassword(newPassword);
        studentRepositary.save(student);

    }else if (user instanceof OpenUser){
        OpenUser openUser=(OpenUser) user;
        openUser.setPassword(newPassword);
        openUserRepository.save(openUser);
    }


    emailService.sendEmail(((user instanceof Student)?((Student) user).getSchoolEmail():((OpenUser)user).getEmail()),
          "Password Reset","Your new password "+newPassword);

    return ResponseEntity.ok("New Password sent to your email.");


}}
