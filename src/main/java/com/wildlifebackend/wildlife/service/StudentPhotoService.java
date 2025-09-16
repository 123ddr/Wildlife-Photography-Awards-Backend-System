package com.wildlifebackend.wildlife.service;

import com.wildlifebackend.wildlife.entitiy.SchoolSubmission;
import com.wildlifebackend.wildlife.entitiy.StudentPhoto;
import com.wildlifebackend.wildlife.repository.SchoolSubmissionRepositry;
import com.wildlifebackend.wildlife.repository.StudentPhotoRepo;
import com.wildlifebackend.wildlife.repository.StudentRepositary;
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
public class StudentPhotoService {

    private final StudentPhotoRepo studentPhotoRepository;
    private final SchoolSubmissionRepositry schoolSubmissionRepository;
    private final StudentRepositary studentRepository;

    @Transactional
    public StudentPhoto createPhotoFromSubmission(Long submissionId) throws IOException {
        SchoolSubmission submission = schoolSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found with id: " + submissionId));

        StudentPhoto photo = new StudentPhoto();

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
            photo.setStudent(submission.getPhotographer());
        }

        photo.setCategory(submission.getCategory());
        photo.setSubmission(submission);

        return studentPhotoRepository.save(photo);
    }

}