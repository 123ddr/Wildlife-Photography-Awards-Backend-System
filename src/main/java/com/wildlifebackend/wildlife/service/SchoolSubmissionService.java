package com.wildlifebackend.wildlife.service;

import com.wildlifebackend.wildlife.dto.response.StudentSubmissionDTO;
import com.wildlifebackend.wildlife.entitiy.Category_School;
import com.wildlifebackend.wildlife.entitiy.SchoolSubmission;
import com.wildlifebackend.wildlife.entitiy.Student;
import com.wildlifebackend.wildlife.repository.Category_SchoolRepository;
import com.wildlifebackend.wildlife.repository.SchoolSubmissionRepositry;
import com.wildlifebackend.wildlife.repository.StudentRepositary;
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
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SchoolSubmissionService {

    private final SchoolSubmissionRepositry schoolSubmissionRepository;
    private final StudentRepositary studentRepository;
    private final Category_SchoolRepository categoryRepository;
    private final Path uploadDir = Paths.get("school-uploads");

    public SchoolSubmissionService(SchoolSubmissionRepositry schoolSubmissionRepository,
                                   StudentRepositary studentRepository,
                                   Category_SchoolRepository categoryRepository) {
        this.schoolSubmissionRepository = schoolSubmissionRepository;
        this.studentRepository = studentRepository;
        this.categoryRepository = categoryRepository;

        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory for school submissions", e);
        }
    }

    public SchoolSubmission createSubmission(StudentSubmissionDTO dto) {
        validateSubmission(dto);

        SchoolSubmission submission = new SchoolSubmission();
        mapDtoToEntity(dto, submission);

        SchoolSubmission savedSubmission = schoolSubmissionRepository.save(submission);
        updateStudentRelationships(savedSubmission);

        return savedSubmission;
    }

    private void mapDtoToEntity(StudentSubmissionDTO dto, SchoolSubmission submission) {
        submission.setEntryTitle(dto.getEntryTitle());
        submission.setDateOfPhotograph(dto.getDateOfPhotograph());
        submission.setTechnicalInfo(dto.getTechnicalInfo());
        submission.setEntryDescription(dto.getEntryDescription());
        submission.setEntryCategories(dto.getEntryCategories());
        submission.setRawFilePath(dto.getRawFilePath());

        // Set students (photographers)
        Set<Student> photographers = studentRepository.findAllById(dto.getPhotographerIds())
                .stream()
                .collect(Collectors.toSet());
        submission.setPhotographers(photographers);

        // Set category if provided
        if (dto.getCategoryId() != null) {
            Category_School category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "School category not found with id: " + dto.getCategoryId()));
            submission.setCategory(category);
        }
    }

    private void updateStudentRelationships(SchoolSubmission submission) {
        submission.getPhotographers().forEach(photographer -> {
            photographer.getSubmissions().add(submission);
            studentRepository.save(photographer);
        });
    }

    private void validateSubmission(StudentSubmissionDTO dto) {
        if (dto.getPhotographerIds() == null || dto.getPhotographerIds().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one student ID is required");
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
            String uniqueFilename = "school-" + UUID.randomUUID() + fileExtension;
            Path targetLocation = uploadDir.resolve(uniqueFilename);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toString();
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store school submission file", ex);
        }
    }
}