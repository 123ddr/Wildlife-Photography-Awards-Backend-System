package com.wildlifebackend.wildlife.controller;


import com.wildlifebackend.wildlife.dto.response.OpenCategoryWinnerResponse;
import com.wildlifebackend.wildlife.dto.response.OpenPhotoScoringRequest;
import com.wildlifebackend.wildlife.dto.response.OpenPhotoSelectionRequest;
import com.wildlifebackend.wildlife.dto.response.OpenWinnerResponse;
import com.wildlifebackend.wildlife.entitiy.OpenJudging;
import com.wildlifebackend.wildlife.service.OpenJudgingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/open-judging")
@RequiredArgsConstructor
public class OpenJudgingController {

    private final OpenJudgingService openJudgingService;

    @PostMapping("/select")
    public ResponseEntity<OpenJudging> selectPhoto(@RequestBody OpenPhotoSelectionRequest request) {
        OpenJudging result = openJudgingService.selectPhoto(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/selected/{judgeId}/{categoryId}")
    public ResponseEntity<List<OpenJudging>> getSelectedPhotos(
            @PathVariable Long judgeId,
            @PathVariable Long categoryId) {
        List<OpenJudging> selectedPhotos = openJudgingService.getSelectedPhotos(judgeId, categoryId);
        return ResponseEntity.ok(selectedPhotos);
    }

    @GetMapping("/unmarked/{judgeId}/{categoryId}")
    public ResponseEntity<List<OpenJudging>> getUnmarkedPhotos(
            @PathVariable Long judgeId,
            @PathVariable Long categoryId) {
        List<OpenJudging> unmarkedPhotos = openJudgingService.getUnmarkedPhotos(judgeId, categoryId);
        return ResponseEntity.ok(unmarkedPhotos);
    }

    @PostMapping("/score")
    public ResponseEntity<OpenJudging> scorePhoto(@RequestBody OpenPhotoScoringRequest request) {
        OpenJudging result = openJudgingService.scorePhoto(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/winners/save")
    public ResponseEntity<List<OpenWinnerResponse>> calculateAndSaveWinners() {
        return ResponseEntity.ok(openJudgingService.calculateAndSaveWinners());
    }

    //winners by category

    @GetMapping("/winners/Animal Behaviours")
    public ResponseEntity<List<OpenCategoryWinnerResponse>> getNatureWinners() {
        return ResponseEntity.ok(openJudgingService.getTopWinnersByCategory("ANIMAL BEHAVIOURS", 5));
    }

    @GetMapping("/winners/Animal Portraits")
    public ResponseEntity<List<OpenCategoryWinnerResponse>> getPortraitWinners() {
        return ResponseEntity.ok(openJudgingService.getTopWinnersByCategory("ANIMAL PORTRAITS", 5));
    }

    @GetMapping("/winners/Natural Habitats")
    public ResponseEntity<List<OpenCategoryWinnerResponse>> getWildlifeWinners() {
        return ResponseEntity.ok(openJudgingService.getTopWinnersByCategory("NATURAL HABITATS", 5));
    }

    @GetMapping("/winners/Urban Wildlife")
    public ResponseEntity<List<OpenCategoryWinnerResponse>> getLandscapeWinners() {
        return ResponseEntity.ok(openJudgingService.getTopWinnersByCategory("URBAN WILDLIFE", 5));
    }

    @GetMapping("/winners/Wild Flora")
    public ResponseEntity<List<OpenCategoryWinnerResponse>> getUrbanWinners() {
        return ResponseEntity.ok(openJudgingService.getTopWinnersByCategory("WILD FLORA", 5));
    }

}
