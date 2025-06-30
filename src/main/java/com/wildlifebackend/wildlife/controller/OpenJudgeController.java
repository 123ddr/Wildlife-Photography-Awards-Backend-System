package com.wildlifebackend.wildlife.controller;


import com.wildlifebackend.wildlife.entitiy.OpenJudge;
import com.wildlifebackend.wildlife.service.OpenJudgeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/judges")
@CrossOrigin(origins = "*")
public class OpenJudgeController {

    private final OpenJudgeService judgeService;

    public OpenJudgeController(OpenJudgeService judgeService) {
        this.judgeService = judgeService;
    }

    @PostMapping
    public ResponseEntity<OpenJudge> createJudge(@RequestBody OpenJudge judge) {
        OpenJudge createdJudge = judgeService.createJudge(judge.getName(), judge.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJudge);
    }

    @GetMapping
    public ResponseEntity<List<OpenJudge>> getAllJudges() {
        return ResponseEntity.ok(judgeService.getAllJudges());
    }

    @GetMapping("/{judgeId}")
    public ResponseEntity<OpenJudge> getJudgeById(@PathVariable Long judgeId) {
        return judgeService.getJudgeById(judgeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{judgeId}")
    public ResponseEntity<OpenJudge> updateJudge(
            @PathVariable Long judgeId,
            @RequestBody OpenJudge judgeDetails) {
        try {
            OpenJudge updatedJudge = judgeService.updateJudge(
                    judgeId,
                    judgeDetails.getName(),
                    judgeDetails.getEmail());
            return ResponseEntity.ok(updatedJudge);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{judgeId}")
    public ResponseEntity<Void> deleteJudge(@PathVariable Long judgeId) {
        try {
            judgeService.deleteJudge(judgeId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
