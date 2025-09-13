package com.wildlifebackend.wildlife.controller;


import com.wildlifebackend.wildlife.service.SubmissionSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionSummaryController {

    private final SubmissionSummaryService submissionSummaryService;

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getTotalSubmissions() {
        try {
            long total = submissionSummaryService.getTotalSubmissions();
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

    // Get Open submission category totals
    @GetMapping("/open/categories")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getOpenCategoryTotals() {
        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "openCategoryTotals", submissionSummaryService.getOpenCategoryTotals()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Internal server error",
                    "message", "Failed to retrieve open submission categories"
            ));
        }
    }

    // Get School submission category totals
    @GetMapping("/school/categories")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getSchoolCategoryTotals() {
        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "schoolCategoryTotals", submissionSummaryService.getSchoolCategoryTotals()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Internal server error",
                    "message", "Failed to retrieve school submission categories"
            ));
        }
    }
}

