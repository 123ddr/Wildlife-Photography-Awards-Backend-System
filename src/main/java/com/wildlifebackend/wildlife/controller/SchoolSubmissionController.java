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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping("/api/schoolsubmission")
@Validated
public class SchoolSubmissionController {

    private final SchoolSubmissionService schoolSubmissionService;
    private final ObjectMapper objectMapper;

    public SchoolSubmissionController(SchoolSubmissionService schoolSubmissionService, ObjectMapper objectMapper) {
        this.schoolSubmissionService = schoolSubmissionService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createSchoolSubmission(
            @RequestPart("data") String submissionJson,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            // Convert JSON to DTO manually
            StudentSubmissionDTO submissionDTO = objectMapper.readValue(submissionJson, StudentSubmissionDTO.class);

            // Attach file if available
            if (file != null && !file.isEmpty()) {
                submissionDTO.setRawFile(file);
            }

            // Save submission
            SchoolSubmission savedSubmission = schoolSubmissionService.saveSchoolSubmission(submissionDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "School submission created successfully",
                    "data", savedSubmission
            ));

        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", "Invalid JSON format in 'data' part",
                    "message", e.getOriginalMessage()
            ));

        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", "Validation failed",
                    "details", e.getConstraintViolations()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "School submission failed",
                    "message", e.getMessage()
            ));
        }
    }
}
