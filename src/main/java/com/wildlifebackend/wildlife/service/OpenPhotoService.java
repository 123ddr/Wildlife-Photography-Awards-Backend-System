package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.entitiy.OpenPhoto;
import com.wildlifebackend.wildlife.entitiy.OpenUser;
import com.wildlifebackend.wildlife.exception.ResourceNotFoundException;
import com.wildlifebackend.wildlife.repository.OpenPhotoRepo;
import com.wildlifebackend.wildlife.repository.OpenUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class OpenPhotoService {

    @Autowired
    private OpenPhotoRepo openPhotoRepo;

    @Autowired
    private OpenUserRepository openuserRepositary;


    public OpenPhoto uploadPhoto(Long openUserId, MultipartFile file, String title, String description) throws IOException {

        // Validate and fetch the user
        OpenUser openUser = openuserRepositary.findById(openUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + openUserId));

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        // Prepare and populate OpenPhoto entity
        OpenPhoto openPhoto = new OpenPhoto();
        openPhoto.setTitle(title);
        openPhoto.setDescription(description);
        openPhoto.setUploadDateTime(LocalDate.now()); // Important!
        openPhoto.setFileData(file.getBytes());
        openPhoto.setOpenUser(openUser);

        return openPhotoRepo.save(openPhoto);
    }


    public OpenPhoto getPhotoById(Long photoId) {
        if (photoId == null || photoId <= 0) {
            throw new IllegalArgumentException("Invalid photo ID provided.");
        }

        return openPhotoRepo.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found with ID: " + photoId));
    }

    public List<OpenPhoto> getAllPhotosByOpenUser(Long openUserId) {
        if (openUserId == null || openUserId <= 0) {
            throw new IllegalArgumentException("Invalid OpenUser ID provided.");
        }

        boolean userExists = openuserRepositary.existsById(openUserId);
        if (!userExists) {
            throw new ResourceNotFoundException("OpenUser not found with ID: " + openUserId);
        }

        return openPhotoRepo.findByOpenUserId(openUserId);
    }

    @Transactional
    public OpenPhoto updatePhotoDetails(Long photoId, String title, String description) {
        OpenPhoto openPhoto = getPhotoById(photoId);

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }

        openPhoto.setTitle(title.trim());
        openPhoto.setDescription(description.trim());

        return openPhotoRepo.save(openPhoto);
    }


    public void deletePhoto(Long photoId) {
        OpenPhoto photo = openPhotoRepo.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found with ID: " + photoId));

        openPhotoRepo.delete(photo);
    }


}
