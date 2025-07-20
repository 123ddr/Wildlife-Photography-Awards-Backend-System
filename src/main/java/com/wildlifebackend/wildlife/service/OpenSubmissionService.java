package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.dto.response.OpenSubmissionDTO;
import com.wildlifebackend.wildlife.entitiy.Category_Open;
import com.wildlifebackend.wildlife.entitiy.OpenSubmission;
import com.wildlifebackend.wildlife.entitiy.OpenUser;
import com.wildlifebackend.wildlife.repository.Category_OpenRepository;
import com.wildlifebackend.wildlife.repository.OpenSubmissionRepository;
import com.wildlifebackend.wildlife.repository.OpenUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
import java.util.stream.Collectors;

@Service
@Transactional
public class OpenSubmissionService {

    private final OpenSubmissionRepository openSubmissionRepository;
    private final OpenUserRepository openUserRepository;
    private final Category_OpenRepository categoryRepository;
    private final Path uploadDir = Paths.get("uploads");

    public OpenSubmissionService(OpenSubmissionRepository openSubmissionRepository,
                                 OpenUserRepository openUserRepository,
                                 Category_OpenRepository categoryRepository) {
        this.openSubmissionRepository = openSubmissionRepository;
        this.openUserRepository = openUserRepository;
        this.categoryRepository = categoryRepository;

        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public OpenSubmission createSubmission(OpenSubmissionDTO dto) {
        validateSubmission(dto);

        OpenSubmission submission = new OpenSubmission();
        mapDtoToEntity(dto, submission);

        OpenSubmission savedSubmission = openSubmissionRepository.save(submission);
        updatePhotographerRelationships(savedSubmission);

        return savedSubmission;
    }

    private void mapDtoToEntity(OpenSubmissionDTO dto, OpenSubmission submission) {
        submission.setEntryTitle(dto.getEntryTitle());
        submission.setDateOfPhotograph(dto.getDateOfPhotograph());
        submission.setTechnicalInfo(dto.getTechnicalInfo());
        submission.setEntryDescription(dto.getEntryDescription());
        submission.setEntryCategories(dto.getEntryCategories());
        submission.setRawFilePath(dto.getRawFilePath());

        // Set photographers
        Set<OpenUser> photographers = openUserRepository.findAllById(dto.getPhotographerIds())
                .stream()
                .collect(Collectors.toSet());
        submission.setPhotographers(photographers);

        // Set category if provided
        if (dto.getCategoryId() != null) {
            Category_Open category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Category not found with id: " + dto.getCategoryId()));
            submission.setCategory(category);
        }
    }

    private void updatePhotographerRelationships(OpenSubmission submission) {
        submission.getPhotographers().forEach(photographer -> {
            photographer.getSubmissions().add(submission);
            openUserRepository.save(photographer);
        });
    }

    private void validateSubmission(OpenSubmissionDTO dto) {
        if (dto.getPhotographerIds() == null || dto.getPhotographerIds().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one photographer ID is required");
        }

        if (dto.getEntryCategories() != null) {
            dto.getEntryCategories().removeIf(String::isBlank);
        }
    }

    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        }

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (originalFilename.contains("..")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file path");
            }

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID() + fileExtension;
            Path targetLocation = uploadDir.resolve(uniqueFilename);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toString();
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store file", ex);
        }
    }
}