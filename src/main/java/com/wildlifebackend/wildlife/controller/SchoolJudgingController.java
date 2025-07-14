package com.wildlifebackend.wildlife.controller;

import com.wildlifebackend.wildlife.dto.response.JudgingUpdateRequest;
import com.wildlifebackend.wildlife.dto.response.SchoolJudgingRequest;
import com.wildlifebackend.wildlife.entitiy.SchoolJudging;
import com.wildlifebackend.wildlife.service.SchoolJudgingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/school/judgings")
@CrossOrigin(origins = "*")
public class SchoolJudgingController {

    private final SchoolJudgingService judgingService;

    public SchoolJudgingController(SchoolJudgingService judgingService) {
        this.judgingService = judgingService;
    }

    @PostMapping
    public ResponseEntity<SchoolJudging> createJudging(@RequestBody SchoolJudgingRequest request) {
        SchoolJudging judging = judgingService.createJudging(
                request.getJudgeId(),
                request.getPhotoId(),
                request.getScore(),
                request.getFeedback()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(judging);
    }

    @GetMapping("/photo/{photoId}")
    public ResponseEntity<List<SchoolJudging>> getJudgingsByPhotoId(@PathVariable Long photoId) {
        return ResponseEntity.ok(judgingService.getJudgingsByPhotoId(photoId));
    }

    @GetMapping("/judge/{judgeId}")
    public ResponseEntity<List<SchoolJudging>> getJudgingsByJudgeId(@PathVariable Long judgeId) {
        return ResponseEntity.ok(judgingService.getJudgingsByJudgeId(judgeId));
    }

    @GetMapping("/photo/{photoId}/average")
    public ResponseEntity<Double> getAverageScoreForPhoto(@PathVariable Long photoId) {
        return judgingService.getAverageScoreForPhoto(photoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{judgingId}")
    public ResponseEntity<SchoolJudging> updateJudging(
            @PathVariable Long judgingId,
            @RequestBody JudgingUpdateRequest updateRequest) {
        try {
            SchoolJudging updatedJudging = judgingService.updateJudging(
                    judgingId,
                    updateRequest.getScore(),
                    updateRequest.getFeedback()
            );
            return ResponseEntity.ok(updatedJudging);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{judgingId}")
    public ResponseEntity<Void> deleteJudging(@PathVariable Long judgingId) {
        try {
            judgingService.deleteJudging(judgingId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}