package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.dto.response.PhotoDTO;
import com.wildlifebackend.wildlife.entitiy.Photo;
import com.wildlifebackend.wildlife.repository.PhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public List<PhotoDTO> getAllPhotos() {
        return photoRepository.findAll().stream()
                .map(photo -> new PhotoDTO(photo.getPhotoId(), photo.getTitle(), photo.getDescription()))
                .collect(Collectors.toList());
    }

    public Optional<PhotoDTO> getPhotoById(Long id) {
        return photoRepository.findById(id)
                .map(photo -> new PhotoDTO(photo.getPhotoId(), photo.getTitle(), photo.getDescription()));
    }

    public Photo savePhoto(MultipartFile file, String title, String description) throws IOException {
        Photo photo = Photo.builder()
                .rawFile(file.getBytes())
                .title(title)
                .description(description)
                .build();
        return photoRepository.save(photo);
    }

    public PhotoDTO updatePhoto(Long id, MultipartFile file, String title, String description) throws IOException {
        return photoRepository.findById(id).map(existingPhoto -> {
            try {
                if (file != null) {
                    existingPhoto.setRawFile(file.getBytes());
                }
                existingPhoto.setTitle(title);
                existingPhoto.setDescription(description);
                Photo updated = photoRepository.save(existingPhoto);
                return new PhotoDTO(updated.getPhotoId(), updated.getTitle(), updated.getDescription());
            } catch (IOException e) {
                throw new RuntimeException("Error updating photo", e);
            }
        }).orElseThrow(() -> new RuntimeException("Photo not found"));
    }

    public void deletePhoto(Long id) {
        photoRepository.deleteById(id);
    }
}
