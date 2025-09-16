package com.wildlifebackend.wildlife.service;



import com.wildlifebackend.wildlife.entitiy.OpenPhoto;
import com.wildlifebackend.wildlife.entitiy.OpenSubmission;
import com.wildlifebackend.wildlife.repository.OpenPhotoRepo;
import com.wildlifebackend.wildlife.repository.OpenSubmissionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



@Service
@Transactional
@RequiredArgsConstructor
public class OpenPhotoService {

    private final OpenPhotoRepo openPhotoRepository;
    private final OpenSubmissionRepository openSubmissionRepository;

    @Transactional
    public OpenPhoto createPhotoFromSubmission(Long submissionId) throws IOException {
        OpenSubmission submission = openSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found with id: " + submissionId));

        OpenPhoto photo = new OpenPhoto();

        // Transfer data from submission to photo
        photo.setTitle(submission.getEntryTitle());
        photo.setDescription(submission.getEntryDescription());

        // Handle file data
        if (submission.getRawFilePath() != null) {
            Path path = Paths.get(submission.getRawFilePath());
            try {
                photo.setFileData(Files.readAllBytes(path));
                // Optionally delete the temp file after transfer
                Files.deleteIfExists(path);
            } catch (IOException e) {
                throw new IOException("Failed to process file: " + e.getMessage(), e);
            }
        }

        // Set relationships - now using single photographer instead of set
        if (submission.getPhotographer() != null) {
            photo.setOpenUser(submission.getPhotographer());
        }

        photo.setCategory(submission.getCategory());
        photo.setSubmission(submission);

        return openPhotoRepository.save(photo);
    }

}
