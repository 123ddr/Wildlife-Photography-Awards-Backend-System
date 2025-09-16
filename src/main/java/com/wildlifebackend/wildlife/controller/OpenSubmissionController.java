package com.wildlifebackend.wildlife.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildlifebackend.wildlife.dto.response.OpenSubmissionDTO;
import com.wildlifebackend.wildlife.entitiy.OpenSubmission;
import com.wildlifebackend.wildlife.service.OpenSubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/api/open_submissions")
@RequiredArgsConstructor
@Validated
public class OpenSubmissionController {
    private final OpenSubmissionService openSubmissionService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('OPENUSER', 'ADMIN')")
    public ResponseEntity<?> createSubmission(
            @RequestPart("data") @Valid String submissionJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            // Parse JSON to DTO
            OpenSubmissionDTO dto = objectMapper.readValue(submissionJson, OpenSubmissionDTO.class);

            // Validate required fields
            if (dto.getPhotographerId() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Validation error",
                        "message", "Photographer ID is required"
                ));
            }

            if (dto.getEntryCategory() == null || dto.getEntryCategory().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Validation error",
                        "message", "Entry category is required"
                ));
            }

            // Create submission
            OpenSubmission submission = openSubmissionService.createSubmission(dto, file);

            // Build success response
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "success",
                    "message", "Submission created successfully",
                    "data", Map.of(
                            "submissionId", submission.getId(),
                            "photographerId", submission.getPhotographerId(),
                            "entryTitle", submission.getEntryTitle(),
                            "entryCategory", submission.getEntryCategory(),
                            "filePath", submission.getRawFilePath()
                    )
            ));

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid request format",
                    "message", "Malformed JSON data"
            ));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of(
                    "error", "Submission error",
                    "message", e.getReason(),
                    "status", e.getStatusCode().value()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Internal server error",
                    "message", "An unexpected error occurred"
            ));
        }
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getTotalSubmissions() {
        try {
            long total = openSubmissionService.getTotalSubmissions();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "totalSubmissions", total
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Internal server error",
                    "message", "Failed to retrieve submission count"
            ));
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getAllSubmissions() {
        try {
            List<OpenSubmission> submissions = openSubmissionService.getAllSubmissions();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", submissions
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Internal server error",
                    "message", "Failed to retrieve submissions"
            ));
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllowedCategories() {
        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", List.of(
                            "Animal Behaviours",
                            "Animal Portraits",
                            "Natural Habitats",
                            "Urban Wildlife",
                            "Wild Flora"
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Internal server error",
                    "message", "Failed to retrieve categories"
            ));
        }
    }
}
