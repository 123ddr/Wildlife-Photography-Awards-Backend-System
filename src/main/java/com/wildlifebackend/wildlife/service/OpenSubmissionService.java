package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.dto.response.OpenSubmissionDTO;
import com.wildlifebackend.wildlife.entitiy.OpenSubmission;
import com.wildlifebackend.wildlife.entitiy.OpenUser;
import com.wildlifebackend.wildlife.repository.OpenSubmissionRepository;
import com.wildlifebackend.wildlife.repository.OpenUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class OpenSubmissionService {

    private final OpenSubmissionRepository openSubmissionRepository;
    private final OpenUserRepository openUserRepository;

    private final Path uploadDir = Paths.get("uploads");

    public OpenSubmissionService(OpenSubmissionRepository openSubmissionRepository, OpenUserRepository openUserRepository) {
        this.openSubmissionRepository = openSubmissionRepository;
        this.openUserRepository = openUserRepository;

        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public OpenSubmission saveSubmission(OpenSubmissionDTO dto) {
        validateDTO(dto);

        OpenSubmission submission = new OpenSubmission();
        submission.setEntryTitle(dto.getEntryTitle());
        submission.setDateOfPhotograph(dto.getDateOfPhotograph());
        submission.setTechnicalInfo(dto.getTechnicalInfo());
        submission.setEntryDescription(dto.getEntryDescription());
        submission.setEntryCategories(dto.getEntryCategories());

        // Set photographers
        Set<OpenUser> photographers = new HashSet<>(openUserRepository.findAllById(dto.getPhotographerIds()));
        if (photographers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No valid photographers found for given IDs");
        }
        submission.setPhotographers(photographers);

        // Maintain bidirectional relationship
        for (OpenUser photographer : photographers) {
            photographer.getSubmissions().add(submission);
        }

        // Handle raw file upload
        MultipartFile rawFile = dto.getRawFile();
        if (rawFile != null && !rawFile.isEmpty()) {
            try {
                String uniqueFilename = UUID.randomUUID() + "_" + Objects.requireNonNull(rawFile.getOriginalFilename());
                Path targetLocation = uploadDir.resolve(uniqueFilename);
                Files.copy(rawFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                submission.setRawFilePath(targetLocation.toString());
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload raw file", e);
            }
        }

        return openSubmissionRepository.save(submission);
    }

    private void validateDTO(OpenSubmissionDTO dto) {
        if (dto.getEntryTitle() == null || dto.getEntryTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entry title is required");
        }
        if (dto.getDateOfPhotograph() == null || dto.getDateOfPhotograph().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date of photograph is invalid");
        }
        if (dto.getTechnicalInfo() == null || dto.getTechnicalInfo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Technical info is required");
        }
        if (dto.getPhotographerIds() == null || dto.getPhotographerIds().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one photographer ID is required");
        }
    }
}
