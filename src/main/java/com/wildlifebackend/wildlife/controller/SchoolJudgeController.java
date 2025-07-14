package com.wildlifebackend.wildlife.controller;

import com.wildlifebackend.wildlife.entitiy.SchoolJudge;
import com.wildlifebackend.wildlife.service.SchoolJudgeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/school/judges")
@CrossOrigin(origins = "*")
public class SchoolJudgeController {

    private final SchoolJudgeService judgeService;

    public SchoolJudgeController(SchoolJudgeService judgeService) {
        this.judgeService = judgeService;
    }

    @PostMapping
    public ResponseEntity<SchoolJudge> createJudge(@RequestBody SchoolJudge judge) {
        SchoolJudge createdJudge = judgeService.createJudge(judge.getName(), judge.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJudge);
    }

    @GetMapping
    public ResponseEntity<List<SchoolJudge>> getAllJudges() {
        return ResponseEntity.ok(judgeService.getAllJudges());
    }

    @GetMapping("/{judgeId}")
    public ResponseEntity<SchoolJudge> getJudgeById(@PathVariable Long judgeId) {
        return judgeService.getJudgeById(judgeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{judgeId}")
    public ResponseEntity<SchoolJudge> updateJudge(
            @PathVariable Long judgeId,
            @RequestBody SchoolJudge judgeDetails) {
        try {
            SchoolJudge updatedJudge = judgeService.updateJudge(
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