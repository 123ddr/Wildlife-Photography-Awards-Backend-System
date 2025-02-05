package com.wildlifebackend.wildlife.controller;


import com.wildlifebackend.wildlife.dto.response.OpenSubmissionDTO;
import com.wildlifebackend.wildlife.entitiy.OpenSubmission;
import com.wildlifebackend.wildlife.service.OpenSubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/submissions")
public class OpenSubmissionController {

    private final OpenSubmissionService openservice;

    public OpenSubmissionController(OpenSubmissionService openservice) {
        this.openservice = openservice;
    }

    @PostMapping("/create")
    public ResponseEntity<OpenSubmission> submitEntry(@ModelAttribute OpenSubmissionDTO dto) {
        OpenSubmission submission = openservice.saveSubmission(dto);
        return ResponseEntity.ok(submission);
    }
}
