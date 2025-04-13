package com.wildlifebackend.wildlife.controller;


import com.wildlifebackend.wildlife.entitiy.OpenPhoto;
import com.wildlifebackend.wildlife.entitiy.OpenUser;
import com.wildlifebackend.wildlife.service.OpenPhotoService;
import com.wildlifebackend.wildlife.service.serviceImpl.OpenUserServiceImpl;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/openphotos")
@CrossOrigin(origins = "*")
public class OpenPhotoController {

    @Autowired
    private OpenPhotoService openPhotoService;

    @Autowired
    private OpenUserServiceImpl openUserService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('OPENUSER')")
    public ResponseEntity<OpenPhoto> uploadPhoto(@RequestParam("file") MultipartFile file,
                                                 @RequestParam String title,
                                                 @RequestParam String description,
                                                 Authentication authentication) throws IOException {
        OpenUser openUser = openUserService.getAuthenticatedUser(authentication);
        return ResponseEntity.ok(openPhotoService.uploadPhoto(openUser.getId(), file, title, description));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpenPhoto> getPhoto(@PathVariable Long id) {
        return ResponseEntity.ok(openPhotoService.getPhotoById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('OPENUSER')")
    public ResponseEntity<List<OpenPhoto>> getUserPhotos(Authentication authentication) {
        OpenUser openUser = openUserService.getAuthenticatedUser(authentication);
        return ResponseEntity.ok(openPhotoService.getAllPhotosByStudent(openUser.getId()));
    }

    @PutMapping("/updatephoto/{id}")
    @PreAuthorize("hasRole('OPENUSER')")
    public ResponseEntity<OpenPhoto> updatePhoto(@PathVariable Long id,
                                                 @RequestParam String title,
                                                 @RequestParam String description) {
        return ResponseEntity.ok(openPhotoService.updatePhotoDetails(id, title, description));
    }

    @DeleteMapping("/deletephoto/{id}")
    @PreAuthorize("hasRole('OPENUSER')")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        openPhotoService.deleteStudentPhoto(id);
        return ResponseEntity.noContent().build();
    }
}
