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
    public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file,
                                         @RequestParam("title") String title,
                                         @RequestParam("description") String description,
                                         Authentication authentication) {
        try {
            // Get the authenticated user
            OpenUser openUser = openUserService.getAuthenticatedUser(authentication);

            // Upload the photo
            OpenPhoto savedPhoto = openPhotoService.uploadPhoto(openUser.getId(), file, title, description);

            // Return the saved photo details
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPhoto);

        } catch (IllegalArgumentException e) {
            // Handle validation issues like empty file or non-image
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to process uploaded file", "details", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error occurred", "details", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPhotoById(@PathVariable("id") Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid photo ID"));
            }

            OpenPhoto photo = openPhotoService.getPhotoById(id);
            return ResponseEntity.ok(photo);

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error occurred", "details", ex.getMessage()));
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('OPENUSER')")
    public ResponseEntity<?> getUserPhotos(Authentication authentication) {
        try {
            // Get the authenticated user
            OpenUser openUser = openUserService.getAuthenticatedUser(authentication);

            // Fetch all photos belonging to the user
            List<OpenPhoto> photos = openPhotoService.getAllPhotosByOpenUser(openUser.getId());

            // Return 200 OK with data or 204 No Content if empty
            return photos.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "No photos found for the user."))
                    : ResponseEntity.ok(photos);

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch user photos", "details", ex.getMessage()));
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

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update photo details", "details", ex.getMessage()));
        }
    }

    @DeleteMapping("/deletephoto/{id}")
    @PreAuthorize("hasRole('OPENUSER')")
    public ResponseEntity<?> deletePhoto(@PathVariable Long id) {
        try {
            openPhotoService.deletePhoto(id);
            return ResponseEntity.ok(Map.of("message", "Photo deleted successfully."));

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete photo", "details", ex.getMessage()));
        }
    }


}
