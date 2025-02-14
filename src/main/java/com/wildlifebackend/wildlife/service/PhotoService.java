package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.entitiy.Photo;
import com.wildlifebackend.wildlife.repository.PhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    public Optional<Photo> getPhotoById(Long id) {
        return photoRepository.findById(id);
    }

    public Photo savePhoto(MultipartFile file, String title, String description) throws IOException {
        Photo photo = Photo.builder()
                .rawFile(file.getBytes())
                .title(title)
                .description(description)
                .build();
        return photoRepository.save(photo);
    }

    public Photo updatePhoto(Long id, MultipartFile file, String title, String description) throws IOException {
        return photoRepository.findById(id).map(existingPhoto -> {
            try {
                if (file != null) {
                    existingPhoto.setRawFile(file.getBytes());
                }
                existingPhoto.setTitle(title);
                existingPhoto.setDescription(description);
                return photoRepository.save(existingPhoto);
            } catch (IOException e) {
                throw new RuntimeException("Error updating photo", e);
            }
        }).orElseThrow(() -> new RuntimeException("Photo not found"));
    }

    public void deletePhoto(Long id) {
        photoRepository.deleteById(id);
    }
}
