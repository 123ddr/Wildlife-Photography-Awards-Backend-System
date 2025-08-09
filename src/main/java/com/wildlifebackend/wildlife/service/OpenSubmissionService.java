package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.dto.response.OpenSubmissionDTO;
import com.wildlifebackend.wildlife.entitiy.Category_Open;
import com.wildlifebackend.wildlife.entitiy.OpenSubmission;
import com.wildlifebackend.wildlife.entitiy.OpenUser;
import com.wildlifebackend.wildlife.repository.Category_OpenRepository;
import com.wildlifebackend.wildlife.repository.OpenSubmissionRepository;
import com.wildlifebackend.wildlife.repository.OpenUserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OpenSubmissionService {

    private final OpenSubmissionRepository openSubmissionRepository;
    private final OpenUserRepository openUserRepository;
    private final Category_OpenRepository categoryRepository;
    private final OpenPhotoService openPhotoService;
    private final Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(uploadDir);
    }

    @Transactional
    public OpenSubmission createSubmission(OpenSubmissionDTO dto, MultipartFile file) {
        try {
            validateSubmission(dto);

            // Handle file upload
            String filePath = null;
            if (file != null && !file.isEmpty()) {
                filePath = uploadFile(file);
            }

            // Get the photographer
            OpenUser photographer = openUserRepository.findById(dto.getPhotographerId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Photographer not found"));

            // Create submission
            OpenSubmission submission = new OpenSubmission();
            submission.setEntryTitle(dto.getEntryTitle());
            submission.setDateOfPhotograph(dto.getDateOfPhotograph());
            submission.setTechnicalInfo(dto.getTechnicalInfo());
            submission.setEntryDescription(dto.getEntryDescription());
            submission.setRawFilePath(filePath != null ? filePath : dto.getRawFilePath());
            submission.setEmail(dto.getEmail());
            submission.setMobileNumber(dto.getMobileNumber());
            submission.setEntryCategory(dto.getEntryCategory());

            // Set both the photographer relationship and formatted ID
            submission.setPhotographer(photographer);
            submission.setPhotographerId(String.format("OpenUser_ID_%04d", photographer.getId()));

            // Handle category relationship
            if (dto.getCategoryId() != null) {
                Category_Open category = categoryRepository.findById(dto.getCategoryId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Category not found"));
                submission.setCategory(category);
            }

            // Save the submission
            OpenSubmission savedSubmission = openSubmissionRepository.save(submission);

            // Add submission to photographer's list
            photographer.getSubmissions().add(savedSubmission);
            openUserRepository.save(photographer);

            // Transfer to OpenPhoto table if file exists
            if (filePath != null || dto.getRawFilePath() != null) {
                openPhotoService.createPhotoFromSubmission(savedSubmission.getId());
            }

            return savedSubmission;

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Failed to create submission: " + e.getMostSpecificCause().getMessage()
            );
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "File processing error: " + e.getMessage()
            );
        }
    }

    private void validateSubmission(OpenSubmissionDTO dto) {
        if (dto.getPhotographerId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Photographer ID is required"
            );
        }

        if (dto.getEntryCategory() == null || dto.getEntryCategory().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Entry category is required"
            );
        }

        // Add additional validations as needed
        if (dto.getEntryTitle() == null || dto.getEntryTitle().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Entry title is required"
            );
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "File is empty"
            );
        }

        String originalFilename = StringUtils.cleanPath(
                Objects.requireNonNull(file.getOriginalFilename())
        );

        if (originalFilename.contains("..")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid file path"
            );
        }

        String fileExtension = originalFilename.substring(
                originalFilename.lastIndexOf(".")
        );
        String uniqueFilename = UUID.randomUUID() + fileExtension;
        Path targetLocation = uploadDir.resolve(uniqueFilename);

        Files.copy(
                file.getInputStream(),
                targetLocation,
                StandardCopyOption.REPLACE_EXISTING
        );

        return targetLocation.toString();
    }
}