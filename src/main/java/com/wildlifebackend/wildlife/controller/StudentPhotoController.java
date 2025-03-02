package com.wildlifebackend.wildlife.controller;


import com.wildlifebackend.wildlife.entitiy.Student;
import com.wildlifebackend.wildlife.entitiy.StudentPhoto;
import com.wildlifebackend.wildlife.service.StudentPhotoService;
import com.wildlifebackend.wildlife.service.serviceImpl.StudentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/studentphotos")
@RequiredArgsConstructor

public class StudentPhotoController {

    private final StudentPhotoService studentPhotoService;
    private final StudentServiceImpl studentService;
    private final StudentServiceImpl studentServiceImpl;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('STUDENT')")

    public ResponseEntity<StudentPhoto> uploadPhoto(@RequestParam("file")MultipartFile file, @RequestParam String title, @RequestParam String description, Authentication authentication) throws IOException {

        Student student=studentServiceImpl.getAuthenticatedStudent(authentication);
        return ResponseEntity.ok(studentPhotoService.uploadPhoto(student.getId(),file,title,description));


    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentPhoto> getPhoto(@PathVariable Long id){
        return ResponseEntity.ok(studentPhotoService.getPhotoById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")

    public ResponseEntity<List<StudentPhoto>> getStudentPhotos(Authentication authentication){
        Student student=studentServiceImpl.getAuthenticatedStudent(authentication);
        return ResponseEntity.ok(studentPhotoService.getAllPhotosByStudent(student.getId()));
    }

    @PutMapping("/updatephoto/{id}")
    @PreAuthorize("hasRole('STUDENT')")

    public ResponseEntity<StudentPhoto> updatePhoto(@PathVariable Long id,@RequestParam String title,@RequestParam String description){
        return ResponseEntity.ok(studentPhotoService.updatePhotoDetails(id,title,description));

    }

    @DeleteMapping("/deletephoto/{id}")
    @PreAuthorize("hasRole('STUDENT')")

    public ResponseEntity<Void> deletePhoto(@PathVariable Long id){
        studentPhotoService.deleteStudentPhoto(id);
        return ResponseEntity.noContent().build();
    }
}
