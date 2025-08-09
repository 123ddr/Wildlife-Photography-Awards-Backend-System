package com.wildlifebackend.wildlife.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildlifebackend.wildlife.dto.response.StudentSubmissionDTO;
import com.wildlifebackend.wildlife.entitiy.SchoolSubmission;
import com.wildlifebackend.wildlife.service.SchoolSubmissionService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/schoolsubmission")
@Validated
public class SchoolSubmissionController {

    private final SchoolSubmissionService schoolSubmissionService;
    private final ObjectMapper objectMapper;

    public SchoolSubmissionController(SchoolSubmissionService schoolSubmissionService,
                                      ObjectMapper objectMapper) {
        this.schoolSubmissionService = schoolSubmissionService;
        this.objectMapper = objectMapper;
    }

    @PreAuthorize("hasAnyRole('SCHOOLUSER', 'ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createSubmission(
            @RequestPart("data") String submissionJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            StudentSubmissionDTO submissionDTO = objectMapper.readValue(submissionJson, StudentSubmissionDTO.class);

            if (file != null && !file.isEmpty()) {
                String filePath = schoolSubmissionService.uploadFile(file);
                submissionDTO.setRawFilePath(filePath);
                submissionDTO.setRawFile(null);
            }

            SchoolSubmission savedSubmission = schoolSubmissionService.createSubmission(submissionDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "School submission created successfully",
                    "data", savedSubmission
            ));

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid JSON format",
                    "details", e.getOriginalMessage()
            ));
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Validation failed",
                    "violations", e.getConstraintViolations().stream()
                            .map(v -> Map.of(
                                    "field", v.getPropertyPath().toString(),
                                    "message", v.getMessage()
                            ))
                            .collect(Collectors.toList())
            ));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of(
                    "error", e.getReason()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "School submission creation failed",
                    "message", e.getMessage()
            ));
        }
    }
}
