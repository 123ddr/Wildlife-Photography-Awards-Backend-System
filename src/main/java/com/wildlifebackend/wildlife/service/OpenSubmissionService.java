package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.dto.response.OpenSubmissionDTO;
import com.wildlifebackend.wildlife.entitiy.OpenSubmission;
import com.wildlifebackend.wildlife.repository.OpenSubmissionRepository;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class OpenSubmissionService {

    private final OpenSubmissionRepository openrepository;
    private final Path uploadDir = Paths.get("uploads");

    public OpenSubmissionService(OpenSubmissionRepository openrepository) {
        this.openrepository = openrepository;
        try {
            Files.createDirectories(uploadDir);
        } catch (Exception e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public OpenSubmission saveSubmission(OpenSubmissionDTO dto) {
        OpenSubmission submission = new OpenSubmission();
        submission.setPhotographerId(dto.getPhotographerId());
        submission.setEntryCategories(dto.getEntryCategories());
        submission.setEntryTitle(dto.getEntryTitle());
        submission.setDateOfPhotograph(dto.getDateOfPhotograph());
        submission.setTechnicalInfo(dto.getTechnicalInfo());
        submission.setEntryDescription(dto.getEntryDescription());

        if (dto.getRawFile() != null && !dto.getRawFile().isEmpty()) {
            try {
                String filename = dto.getRawFile().getOriginalFilename();
                Path targetLocation = uploadDir.resolve(filename);
                Files.copy(dto.getRawFile().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                submission.setRawFilePath(targetLocation.toString());
            } catch (Exception e) {
                throw new RuntimeException("File upload failed", e);
            }
        }

        return openrepository.save(submission);
    }
}
