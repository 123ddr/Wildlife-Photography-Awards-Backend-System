package com.wildlifebackend.wildlife.service;

import com.wildlifebackend.wildlife.dto.response.StudentSubmissionDTO;
import com.wildlifebackend.wildlife.entitiy.SchoolSubmission;
import com.wildlifebackend.wildlife.repository.SchoolSubmissionRepositry;
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
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

@Service
public class SchoolSubmissionService {

    private final SchoolSubmissionRepositry schoolSubmissionRepository;
    private final Path uploadDir = Paths.get("uploads");

    public SchoolSubmissionService(SchoolSubmissionRepositry schoolSubmissionRepository) {
        this.schoolSubmissionRepository = schoolSubmissionRepository;

        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public SchoolSubmission saveSchoolSubmission(StudentSubmissionDTO dto) {
        validateDTO(dto);

        SchoolSubmission submission = new SchoolSubmission();
        submission.setStudentName(dto.getStudentName());
        submission.setDateOfBirth(dto.getDateOfBirth());
        submission.setContactNo(dto.getContactNo());
        submission.setEntryTitle(dto.getEntryTitle());
        submission.setDateOfPhotograph(dto.getDateOfPhotograph());
        submission.setTechnicalInformation(dto.getTechnicalInformation());
        submission.setEntryDescription(dto.getEntryDescription());
        submission.setEntryCategories(dto.getEntryCategories());

        // Handle raw file upload
        if (dto.getRawFile() != null && !dto.getRawFile().isEmpty()) {
            String rawFilePath = storeFile(dto.getRawFile());
            submission.setRawFilePath(rawFilePath);
        }

        return schoolSubmissionRepository.save(submission);
    }

    private String storeFile(MultipartFile file) {
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
        String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
        Path targetLocation = uploadDir.resolve(uniqueFilename);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toString();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload raw file", e);
        }
    }

    private void validateDTO(StudentSubmissionDTO dto) {
        if (dto.getStudentName() == null || dto.getStudentName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student name is required");
        }

        // Convert Date to LocalDate for comparison
        if (dto.getDateOfBirth() != null) {
            LocalDate dob = dto.getDateOfBirth().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            if (dob.isAfter(LocalDate.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date of birth is invalid");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date of birth is required");
        }

        if (dto.getContactNo() == null || dto.getContactNo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contact number is required");
        }
        if (dto.getEntryTitle() == null || dto.getEntryTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entry title is required");
        }

        // Convert Date to LocalDate for comparison
        if (dto.getDateOfPhotograph() != null) {
            LocalDate photoDate = dto.getDateOfPhotograph().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            if (photoDate.isAfter(LocalDate.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date of photograph is invalid");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date of photograph is required");
        }

        if (dto.getTechnicalInformation() == null || dto.getTechnicalInformation().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Technical information is required");
        }
    }
}