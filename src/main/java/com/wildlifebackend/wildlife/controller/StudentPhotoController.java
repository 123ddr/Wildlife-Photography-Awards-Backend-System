package com.wildlifebackend.wildlife.controller;

import com.wildlifebackend.wildlife.entitiy.Student;
import com.wildlifebackend.wildlife.entitiy.StudentPhoto;
import com.wildlifebackend.wildlife.exception.ResourceNotFoundException;
import com.wildlifebackend.wildlife.service.StudentPhotoService;
import com.wildlifebackend.wildlife.service.serviceImpl.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/studentphotos")
@CrossOrigin(origins = "*")
public class StudentPhotoController {

    @Autowired
    private StudentPhotoService studentPhotoService;

    @Autowired
    private StudentServiceImpl studentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<?> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            Authentication authentication) {
        try {
            // Get authenticated student
            Student student = studentService.getAuthenticatedStudent(authentication);
            if (student == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Student not authenticated"));
            }

            // Upload photo
            StudentPhoto savedPhoto = studentPhotoService.uploadPhoto(student.getId(), file, title, description);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPhoto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "File processing error", "details", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Unexpected error", "details", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<?> getPhotoById(@PathVariable Long id) {
        try {
            StudentPhoto photo = studentPhotoService.getPhotoById(id);
            return ResponseEntity.ok(photo);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to fetch photo", "details", e.getMessage()));
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<?> getStudentPhotos(Authentication authentication) {
        try {
            Student student = studentService.getAuthenticatedStudent(authentication);
            List<StudentPhoto> photos = studentPhotoService.getAllPhotosByStudent(student.getId());

            return photos.isEmpty()
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.ok(photos);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to fetch photos", "details", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<?> updatePhoto(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String description) {
        try {
            StudentPhoto updatedPhoto = studentPhotoService.updatePhotoDetails(id, title, description);
            return ResponseEntity.ok(updatedPhoto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Update failed", "details", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<?> deletePhoto(@PathVariable Long id) {
        try {
            studentPhotoService.deletePhoto(id);
            return ResponseEntity.ok(Map.of("message", "Photo deleted successfully"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Deletion failed", "details", e.getMessage()));
        }
    }
}