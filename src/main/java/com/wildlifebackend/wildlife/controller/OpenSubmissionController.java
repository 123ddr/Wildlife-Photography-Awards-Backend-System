package com.wildlifebackend.wildlife.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildlifebackend.wildlife.dto.response.OpenSubmissionDTO;
import com.wildlifebackend.wildlife.entitiy.OpenSubmission;
import com.wildlifebackend.wildlife.service.OpenSubmissionService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;




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
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            // Convert JSON to DTO manually
            OpenSubmissionDTO submissionDTO = objectMapper.readValue(submissionJson, OpenSubmissionDTO.class);

            // Attach file if available
            if (file != null && !file.isEmpty()) {
                submissionDTO.setRawFile(file);
            }

            // Save submission
            OpenSubmission savedSubmission = openSubmissionService.saveSubmission(submissionDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Submission created successfully",
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
                    "error", "Submission failed",
                    "message", e.getMessage()
            ));
        }
    }


}
