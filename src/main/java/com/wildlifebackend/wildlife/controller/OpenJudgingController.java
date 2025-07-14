package com.wildlifebackend.wildlife.controller;


import com.wildlifebackend.wildlife.dto.response.JudgingUpdateRequest;
import com.wildlifebackend.wildlife.dto.response.OpenJudgingRequest;
import com.wildlifebackend.wildlife.entitiy.OpenJudging;
import com.wildlifebackend.wildlife.service.OpenJudgingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/judgings")
@CrossOrigin(origins = "*")
public class OpenJudgingController {

    private final OpenJudgingService judgingService;

    public OpenJudgingController(OpenJudgingService judgingService) {
        this.judgingService = judgingService;
    }

    @PostMapping
    public ResponseEntity<OpenJudging> createJudging(@RequestBody OpenJudgingRequest request) {
        OpenJudging judging = judgingService.createJudging(
                request.getJudgeId(),
                request.getPhotoId(),
                request.getScore(),
                request.getFeedback()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(judging);
    }

    @GetMapping("/photo/{photoId}")
    public ResponseEntity<List<OpenJudging>> getJudgingsByPhotoId(@PathVariable Long photoId) {
        return ResponseEntity.ok(judgingService.getJudgingsByPhotoId(photoId));
    }

    @GetMapping("/judge/{judgeId}")
    public ResponseEntity<List<OpenJudging>> getJudgingsByJudgeId(@PathVariable Long judgeId) {
        return ResponseEntity.ok(judgingService.getJudgingsByJudgeId(judgeId));
    }

    @GetMapping("/photo/{photoId}/average")
    public ResponseEntity<Double> getAverageScoreForPhoto(@PathVariable Long photoId) {
        return judgingService.getAverageScoreForPhoto(photoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{judgingId}")
    public ResponseEntity<OpenJudging> updateJudging(
            @PathVariable Long judgingId,
            @RequestBody JudgingUpdateRequest updateRequest) {
        try {
            OpenJudging updatedJudging = judgingService.updateJudging(
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
