package com.wildlifebackend.wildlife.service;

import com.wildlifebackend.wildlife.entitiy.Student;
import com.wildlifebackend.wildlife.entitiy.StudentPhoto;
import com.wildlifebackend.wildlife.exception.ResourceNotFoundException;
import com.wildlifebackend.wildlife.repository.StudentPhotoRepo;
import com.wildlifebackend.wildlife.repository.StudentRepositary;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class StudentPhotoService {

    @Autowired
    private StudentPhotoRepo studentPhotoRepo;

    @Autowired
    private StudentRepositary studentRepositary;

    public StudentPhoto uploadPhoto(Long studentId, MultipartFile file, String title, String description) throws IOException {
        // Validate student exists
        Student student = studentRepositary.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        // Create and save photo
        StudentPhoto photo = new StudentPhoto();
        photo.setTitle(title);
        photo.setDescription(description);
        photo.setUploadDateTime(LocalDate.now());
        photo.setFileData(file.getBytes());
        photo.setStudent(student); // Associate with student

        return studentPhotoRepo.save(photo);
    }

    public StudentPhoto getPhotoById(Long photoId) {
        return studentPhotoRepo.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found with ID: " + photoId));
    }

    public List<StudentPhoto> getAllPhotosByStudent(Long studentId) {
        if (!studentRepositary.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with ID: " + studentId);
        }
        return studentPhotoRepo.findByStudentId(studentId);
    }

    @Transactional
    public StudentPhoto updatePhotoDetails(Long photoId, String title, String description) {
        StudentPhoto photo = getPhotoById(photoId);

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }

        photo.setTitle(title.trim());
        photo.setDescription(description.trim());

        return studentPhotoRepo.save(photo);
    }

    public void deletePhoto(Long photoId) {
        StudentPhoto photo = getPhotoById(photoId);
        studentPhotoRepo.delete(photo);
    }
}