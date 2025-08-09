package com.wildlifebackend.wildlife.service;

import com.wildlifebackend.wildlife.dto.response.StudentSubmissionDTO;
import com.wildlifebackend.wildlife.entitiy.Category_School;
import com.wildlifebackend.wildlife.entitiy.SchoolSubmission;
import com.wildlifebackend.wildlife.entitiy.Student;
import com.wildlifebackend.wildlife.repository.*;
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
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolSubmissionService {

    private final SchoolSubmissionRepositry schoolSubmissionRepository;
    private final StudentRepositary studentRepository;
    private final Category_SchoolRepository categoryRepository;
    private final StudentPhotoService studentPhotoService;
    private final Path uploadDir = Paths.get("school-uploads").toAbsolutePath().normalize();

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(uploadDir);
    }

    @Transactional
    public SchoolSubmission createSubmission(StudentSubmissionDTO dto, MultipartFile file) {
        try {
            validateSubmission(dto);

            // Handle file upload
            String filePath = null;
            if (file != null && !file.isEmpty()) {
                filePath = uploadFile(file);
            }

            // Get the primary student
            Student photographer = studentRepository.findById(dto.getPhotographerId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Photographer not found"));

            // Create submission
            SchoolSubmission submission = new SchoolSubmission();
            submission.setEntryTitle(dto.getEntryTitle());
            submission.setDateOfPhotograph(dto.getDateOfPhotograph());
            submission.setTechnicalInfo(dto.getTechnicalInfo());
            submission.setEntryDescription(dto.getEntryDescription());
            submission.setRawFilePath(filePath != null ? filePath : dto.getRawFilePath());
            submission.setEmail(dto.getEmail());
            submission.setMobileNumber(dto.getMobileNumber());

            submission.setEntryCategory(dto.getEntryCategory());

            // Set both the student relationship and formatted ID
            submission.setPhotographer(photographer);
            submission.setPhotographerId(String.format("Student_ID_%04d", photographer.getId()));

            // Handle category relationship
            if (dto.getCategoryId() != null) {
                Category_School category = categoryRepository.findById(dto.getCategoryId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Category not found"));
                submission.setCategory(category);
            }

//            // Set photographers if provided
//            if (dto.getPhotographerId() != null && !dto.getPhotographerId().isEmpty()) {
//                Set<Student> photographers = studentRepository.findAllById(dto.getPhotographerIds())
//                        .stream()
//                        .collect(Collectors.toSet());
//                submission.setPhotographers(photographers);
//            }

            // Save the submission
            SchoolSubmission savedSubmission = schoolSubmissionRepository.save(submission);

            // Add submission to photographer's list
            photographer.getSubmissions().add(savedSubmission);
            studentRepository.save(photographer);

//            // Update photographers' relationships
//            if (savedSubmission.getPhotographers() != null) {
//                savedSubmission.getPhotographers().forEach(photographer -> {
//                    photographer.getPhotographedSubmissions().add(savedSubmission);
//                    studentRepository.save(photographer);
//                });
//            }

            // Transfer to StudentPhoto table if file exists
            if (filePath != null || dto.getRawFilePath() != null) {
                studentPhotoService.createPhotoFromSubmission(savedSubmission.getId());
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

    private void validateSubmission(StudentSubmissionDTO dto) {
        if (dto.getPhotographerId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Student ID is required"
            );
        }

        if (dto.getEntryCategory() == null || dto.getEntryCategory().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "At least one entry category is required"
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
        String uniqueFilename = "school-" + UUID.randomUUID() + fileExtension;
        Path targetLocation = uploadDir.resolve(uniqueFilename);

        Files.copy(
                file.getInputStream(),
                targetLocation,
                StandardCopyOption.REPLACE_EXISTING
        );

        return targetLocation.toString();
    }
}