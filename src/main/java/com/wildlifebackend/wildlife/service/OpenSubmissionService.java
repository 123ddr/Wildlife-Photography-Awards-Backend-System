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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OpenSubmissionService {

    private final OpenSubmissionRepository openSubmissionRepository;
    private final OpenUserRepository openUserRepository;
    private final Category_OpenRepository categoryRepository;
    private final OpenPhotoService openPhotoService;
    private final CertificateService certificateService;
    private final EmailService emailService;
    private final Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(uploadDir);
    }

    @Transactional
    public OpenSubmission createSubmission(OpenSubmissionDTO dto, MultipartFile file) {
        try {
            validateSubmission(dto);

            String filePath = null;
            if (file != null && !file.isEmpty()) {
                filePath = uploadFile(file);
            }

            OpenUser photographer = openUserRepository.findById(dto.getPhotographerId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Photographer not found"));

            OpenSubmission submission = new OpenSubmission();
            submission.setEntryTitle(dto.getEntryTitle());
            submission.setDateOfPhotograph(dto.getDateOfPhotograph());
            submission.setTechnicalInfo(dto.getTechnicalInfo());
            submission.setEntryDescription(dto.getEntryDescription());
            submission.setRawFilePath(filePath != null ? filePath : dto.getRawFilePath());
            submission.setEmail(dto.getEmail());
            submission.setMobileNumber(dto.getMobileNumber());
            submission.setEntryCategory(dto.getEntryCategory());

            submission.setPhotographer(photographer);
            submission.setPhotographerId(String.format("OpenUser_ID_%04d", photographer.getId()));

            if (dto.getCategoryId() != null) {
                Category_Open category = categoryRepository.findById(dto.getCategoryId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Category not found"));
                submission.setCategory(category);
            }

            OpenSubmission savedSubmission = openSubmissionRepository.save(submission);

            // Add submission to photographer entity (if you track it)
            photographer.getSubmissions().add(savedSubmission);
            openUserRepository.save(photographer);

            if (filePath != null || dto.getRawFilePath() != null) {
                openPhotoService.createPhotoFromSubmission(savedSubmission.getId());
            }

            // ---- Generate certificate and send email ----
            try {
                File cert = certificateService.generateCertificate(savedSubmission);

                String htmlBody = "<p>Dear " + savedSubmission.getPhotographer().getName() + ",</p>" +
                        "<p>Thank you for your submission \"" + savedSubmission.getEntryTitle() + "\".</p>" +
                        "<p>Please find your participation certificate attached.</p>" +
                        "<p>Best regards,<br/>Organizers</p>";

                emailService.sendCertificateEmail(savedSubmission.getEmail(),
                        "Your Wildlife Photography Submission Certificate",
                        htmlBody,
                        cert);
            } catch (Exception ex) {
                // Log the error (use your logging framework); do not throw unless you want to rollback the DB save.
                System.err.println("Failed to generate/send certificate: " + ex.getMessage());
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

    //get total count of submissions
    public long getTotalSubmissions() {
        return openSubmissionRepository.count();
    }

    // Get all Open Submissions
    public List<OpenSubmission> getAllSubmissions() {
        return openSubmissionRepository.findAll();
    }

}