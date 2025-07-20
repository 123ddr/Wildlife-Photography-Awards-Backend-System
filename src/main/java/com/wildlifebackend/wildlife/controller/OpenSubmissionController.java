package com.wildlifebackend.wildlife.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildlifebackend.wildlife.dto.response.OpenSubmissionDTO;
import com.wildlifebackend.wildlife.entitiy.OpenSubmission;
import com.wildlifebackend.wildlife.service.OpenSubmissionService;
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
@RequestMapping("/api/open_submissions")
@Validated
public class OpenSubmissionController {

    private final OpenSubmissionService openSubmissionService;
    private final ObjectMapper objectMapper;

    public OpenSubmissionController(OpenSubmissionService openSubmissionService, ObjectMapper objectMapper) {
        this.openSubmissionService = openSubmissionService;
        this.objectMapper = objectMapper;
    }

    @PreAuthorize("hasAnyRole('OPENUSER', 'ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createSubmission(
            @RequestPart("data") String submissionJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            OpenSubmissionDTO submissionDTO = objectMapper.readValue(submissionJson, OpenSubmissionDTO.class);

            if (file != null && !file.isEmpty()) {
                // Upload the file and get the stored file path
                String filePath = openSubmissionService.uploadFile(file);
                // Set the file path in the DTO
                submissionDTO.setRawFilePath(filePath);
                // Clear the rawFile field as we don't need to store the MultipartFile
                submissionDTO.setRawFile(null);
            }

            OpenSubmission savedSubmission = openSubmissionService.createSubmission(submissionDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Submission created successfully",
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
                    "error", "Submission creation failed",
                    "message", e.getMessage()
            ));
        }
    }


}
