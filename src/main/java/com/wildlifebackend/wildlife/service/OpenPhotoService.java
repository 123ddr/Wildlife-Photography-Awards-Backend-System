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

        // 1. Fetch user, throw exception if not found
        OpenUser openUser = openuserRepositary.findById(openUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + openUserId));

        // 2. Validate file is not empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // 3. Validate file is an image
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        // 4. Create OpenPhoto entity and set properties
        OpenPhoto openPhoto = new OpenPhoto();
        openPhoto.setTitle(title);
        openPhoto.setDescription(description);
        openPhoto.setUploadDateTime(LocalDate.now());
        openPhoto.setFileData(file.getBytes());

        // 5. Associate the OpenPhoto with the fetched OpenUser
        openPhoto.setOpenUser(openUser);

        // 6. Save and return the entity
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
