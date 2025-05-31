package com.wildlifebackend.wildlife.controller;


import com.wildlifebackend.wildlife.entitiy.OpenPhoto;
import com.wildlifebackend.wildlife.entitiy.OpenUser;
import com.wildlifebackend.wildlife.exception.ResourceNotFoundException;
import com.wildlifebackend.wildlife.service.OpenPhotoService;
import com.wildlifebackend.wildlife.service.serviceImpl.OpenUserServiceImpl;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/openphotos")
@CrossOrigin(origins = "*")
public class OpenPhotoController {

    @Autowired
    private OpenPhotoService openPhotoService;

    @Autowired
    private OpenUserServiceImpl openUserService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('OPENUSER')")
    public ResponseEntity<OpenPhoto> uploadPhoto(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("title") String title,
                                                 @RequestParam("description") String description,
                                                 Authentication authentication) {
        try {
            OpenUser openUser = openUserService.getAuthenticatedUser(authentication);
            OpenPhoto savedPhoto = openPhotoService.uploadPhoto(openUser.getId(), file, title, description);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedPhoto);

        } catch (IOException ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to read uploaded file",
                    ex
            );
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<OpenPhoto> getPhoto(@PathVariable Long id) {
        try {
            OpenPhoto photo = openPhotoService.getPhotoById(id);
            return ResponseEntity.ok(photo);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Or return a custom error response object
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Or return a custom error response object
        }
    }


    @GetMapping
    @PreAuthorize("hasRole('OPENUSER')")
    public ResponseEntity<?> getUserPhotos(Authentication authentication) {
        try {
            OpenUser openUser = openUserService.getAuthenticatedUser(authentication);
            List<OpenPhoto> photos = openPhotoService.getAllPhotosByStudent(openUser.getId());

            if (photos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No photos found for the user.");
            }

            return ResponseEntity.ok(photos);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch user photos."));
        }
    }


    @PutMapping("/updatephoto/{id}")
    @PreAuthorize("hasRole('OPENUSER')")
    public ResponseEntity<?> updatePhoto(@PathVariable Long id,
                                         @RequestParam String title,
                                         @RequestParam String description) {
        try {
            OpenPhoto updatedPhoto = openPhotoService.updatePhotoDetails(id, title, description);
            return ResponseEntity.ok(updatedPhoto);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update photo details."));
        }
    }


    @DeleteMapping("/deletephoto/{id}")
    @PreAuthorize("hasRole('OPENUSER')")
    public ResponseEntity<?> deletePhoto(@PathVariable Long id) {
        try {
            openPhotoService.deleteStudentPhoto(id);
            return ResponseEntity.ok(Map.of("message", "Photo deleted successfully."));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete photo."));
        }
    }

}
