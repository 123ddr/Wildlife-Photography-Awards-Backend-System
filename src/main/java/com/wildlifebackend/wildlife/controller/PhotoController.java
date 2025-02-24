package com.wildlifebackend.wildlife.controller;


import com.wildlifebackend.wildlife.dto.response.PhotoDTO;
import com.wildlifebackend.wildlife.entitiy.Photo;
import com.wildlifebackend.wildlife.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/photos")
@CrossOrigin(origins = "*")
public class PhotoController {

    @Autowired
    private PhotoService photoService;



//    allow you to retrieve all photos of a user by calling
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Photo>> getPhotosByUser(@PathVariable Long userId) {
        List<Photo> photos = photoService.getPhotosByUser(userId);
        return ResponseEntity.ok(photos);
    }



    @GetMapping
    public ResponseEntity<List<PhotoDTO>> getAllPhotos() {
        return ResponseEntity.ok(photoService.getAllPhotos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoDTO> getPhotoById(@PathVariable Long id) {
        return photoService.getPhotoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file,
                                         @RequestParam("title") String title,
                                         @RequestParam("description") String description) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File cannot be empty");
            }

            Photo savedPhoto = photoService.savePhoto(file, title, description);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new PhotoDTO(savedPhoto.getPhotoId(), savedPhoto.getTitle(), savedPhoto.getDescription()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving photo: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePhoto(@PathVariable Long id,
                                         @RequestParam(value = "file", required = false) MultipartFile file,
                                         @RequestParam("title") String title,
                                         @RequestParam("description") String description) {
        try {
            PhotoDTO updatedPhoto = photoService.updatePhoto(id, file, title, description);
            return ResponseEntity.ok(updatedPhoto);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating photo: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Photo not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePhoto(@PathVariable Long id) {
        try {
            photoService.deletePhoto(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Photo not found");
        }
    }
}
