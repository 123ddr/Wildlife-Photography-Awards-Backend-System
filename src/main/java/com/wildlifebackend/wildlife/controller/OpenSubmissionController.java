package com.wildlifebackend.wildlife.controller;


import com.wildlifebackend.wildlife.dto.response.OpenSubmissionDTO;
import com.wildlifebackend.wildlife.entitiy.OpenSubmission;
import com.wildlifebackend.wildlife.service.OpenSubmissionService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;




@RestController
@RequestMapping("/api/open_submissions")
public class OpenSubmissionController {

    private final OpenSubmissionService openSubmissionService;

    public OpenSubmissionController(OpenSubmissionService openSubmissionService) {
        this.openSubmissionService = openSubmissionService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createSubmission(
            @RequestPart("data") @Valid OpenSubmissionDTO submissionDTO,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            // Attach file to DTO
            submissionDTO.setRawFile(file);

            // Save the submission
            OpenSubmission savedSubmission = openSubmissionService.saveSubmission(submissionDTO);

            // Return created submission
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSubmission);

        } catch (Exception e) {
            // Return structured error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", "Submission failed",
                    "message", e.getMessage()
            ));
        }
    }
}
