package com.wildlifebackend.wildlife.controller;

import com.wildlifebackend.wildlife.dto.response.*;
import com.wildlifebackend.wildlife.entitiy.SchoolJudging;
import com.wildlifebackend.wildlife.service.SchoolJudgingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/school-judging")
@RequiredArgsConstructor
public class SchoolJudgingController {

    private final SchoolJudgingService judgingService;

    @PostMapping("/select")
    public ResponseEntity<SchoolJudging> selectPhoto(@RequestBody SchoolPhotoSelectionRequest request) {
        SchoolJudging result = judgingService.selectPhoto(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/selected/{judgeId}/{categoryId}")
    public ResponseEntity<List<SchoolJudging>> getSelectedPhotos(
            @PathVariable Long judgeId,
            @PathVariable Long categoryId) {
        List<SchoolJudging> selectedPhotos = judgingService.getSelectedPhotos(judgeId, categoryId);
        return ResponseEntity.ok(selectedPhotos);
    }

    @GetMapping("/unmarked/{judgeId}/{categoryId}")
    public ResponseEntity<List<SchoolJudging>> getUnmarkedPhotos(
            @PathVariable Long judgeId,
            @PathVariable Long categoryId) {
        List<SchoolJudging> unmarkedPhotos = judgingService.getUnmarkedPhotos(judgeId, categoryId);
        return ResponseEntity.ok(unmarkedPhotos);
    }

    @PostMapping("/score")
    public ResponseEntity<SchoolJudging> scorePhoto(@RequestBody SchoolPhotoScoringRequest request) {
        SchoolJudging result = judgingService.scorePhoto(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/winners/save")
    public ResponseEntity<List<SchoolWinnerResponse>> calculateAndSaveWinners() {
        return ResponseEntity.ok(judgingService.calculateAndSaveWinners());
    }

    //winners by category

    @GetMapping("/winners/Animal Behaviours")
    public ResponseEntity<List<SchoolCategoryWinnerResponse>> getNatureWinners() {
        return ResponseEntity.ok(judgingService.getTopWinnersByCategory("ANIMAL BEHAVIOURS", 5));
    }

    @GetMapping("/winners/Animal Portraits")
    public ResponseEntity<List<SchoolCategoryWinnerResponse>> getPortraitWinners() {
        return ResponseEntity.ok(judgingService.getTopWinnersByCategory("ANIMAL PORTRAITS", 5));
    }

    @GetMapping("/winners/Natural Habitats")
    public ResponseEntity<List<SchoolCategoryWinnerResponse>> getWildlifeWinners() {
        return ResponseEntity.ok(judgingService.getTopWinnersByCategory("NATURAL HABITATS", 5));
    }

    @GetMapping("/winners/Urban Wildlife")
    public ResponseEntity<List<SchoolCategoryWinnerResponse>> getLandscapeWinners() {
        return ResponseEntity.ok(judgingService.getTopWinnersByCategory("URBAN WILDLIFE", 5));
    }

    @GetMapping("/winners/Wild Flora")
    public ResponseEntity<List<SchoolCategoryWinnerResponse>> getUrbanWinners() {
        return ResponseEntity.ok(judgingService.getTopWinnersByCategory("WILD FLORA", 5));
    }

}