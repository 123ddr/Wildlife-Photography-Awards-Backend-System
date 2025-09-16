package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.dto.response.OpenCategoryWinnerResponse;
import com.wildlifebackend.wildlife.dto.response.OpenPhotoScoringRequest;
import com.wildlifebackend.wildlife.dto.response.OpenPhotoSelectionRequest;
import com.wildlifebackend.wildlife.dto.response.OpenWinnerResponse;
import com.wildlifebackend.wildlife.entitiy.*;
import com.wildlifebackend.wildlife.repository.OpenJudgeRepo;
import com.wildlifebackend.wildlife.repository.OpenJudgingCategoryRepository;
import com.wildlifebackend.wildlife.repository.OpenJudgingRepository;
import com.wildlifebackend.wildlife.repository.OpenWinnersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
public class OpenJudgingService {

    private final OpenJudgingRepository judgingRepository;
    private final OpenJudgeRepo judgeRepository;
    private final OpenJudgingCategoryRepository categoryRepository;
    private final OpenWinnersRepo winnersRepository;

    @Transactional
    public OpenJudging selectPhoto(OpenPhotoSelectionRequest request) {
        OpenJudge judge = judgeRepository.findById(request.getJudgeId())
                .orElseThrow(() -> new RuntimeException("Open Judge not found"));

        OpenJudgingCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Open Judging Category not found"));

        // Check if already exists
        OpenJudging judging = judgingRepository
                .findByJudgeJudgeIdAndPhotoIdAndCategoryCategoryId(
                        request.getJudgeId(), request.getPhotoId(), request.getCategoryId())
                .orElseGet(() -> new OpenJudging(judge, request.getPhotoId(), category));

        judging.setIsSelected(request.getIsSelected());
        return judgingRepository.save(judging);
    }

    public List<OpenJudging> getSelectedPhotos(Long judgeId, Long categoryId) {
        return judgingRepository.findByJudgeJudgeIdAndCategoryCategoryIdAndIsSelectedTrue(judgeId, categoryId);
    }

    public List<OpenJudging> getUnmarkedPhotos(Long judgeId, Long categoryId) {
        return judgingRepository.findUnmarkedSelectedPhotos(judgeId, categoryId);
    }

    @Transactional
    public OpenJudging scorePhoto(OpenPhotoScoringRequest request) {
        OpenJudging judging = judgingRepository.findById(request.getJudgingId())
                .orElseThrow(() -> new RuntimeException("Open Judging record not found"));

        // Set individual scores
        judging.setCreativity(request.getCreativity());
        judging.setComposition(request.getComposition());
        judging.setLighting(request.getLighting());
        judging.setFocus(request.getFocus());
        judging.setOriginality(request.getOriginality());
        judging.setTechnicalQuality(request.getTechnicalQuality());
        judging.setImpact(request.getImpact());
        judging.setSubjectMatter(request.getSubjectMatter());

        // Calculate and set total score
        judging.setScore(request.calculateTotalScore());
        judging.setFeedback(request.getFeedback());
        judging.setIsMarked(true);

        return judgingRepository.save(judging);
    }

    @Transactional
    public List<OpenWinnerResponse> calculateAndSaveWinners() {
        List<OpenJudging> judgings = judgingRepository.findAllByOrderByScoreDesc();
        List<OpenWinnerResponse> winners = new ArrayList<>();

        // Clear old winners
        winnersRepository.deleteAll();

        int placeCounter = 1;
        for (OpenJudging j : judgings) {
            OpenPhoto photo = j.getPhoto();
            OpenUser photographer = photo.getOpenUser();
            Category_Open category = photo.getCategory();

            String place = getPlace(placeCounter);

            OpenWinnerResponse response = new OpenWinnerResponse(
                    photographer.getPhotographerId(),
                    photographer.getName(),
                    category != null ? category.getCategoryName() : "Uncategorized",
                    j.getScore(),
                    place
            );

            winners.add(response);

            // Save to DB
            OpenWinners winnerEntity = new OpenWinners();
            winnerEntity.setPhotographerId(response.getPhotographerId());
            winnerEntity.setPhotographerName(response.getPhotographerName());
            winnerEntity.setCategoryName(response.getCategoryName());
            winnerEntity.setScore(response.getScore());
            winnerEntity.setPlace(response.getPlace());

            winnersRepository.save(winnerEntity);

            placeCounter++;
        }

        return winners;
    }

    private String getPlace(int position) {
        return switch (position) {
            case 1 -> "1st Place";
            case 2 -> "2nd Place";
            case 3 -> "3rd Place";
            default -> position + "th Place";
        };
    }

    @Transactional
    public List<OpenCategoryWinnerResponse> getTopWinnersByCategory(String categoryName, int limit) {
        // Use the new repository method
        List<OpenJudging> judgings = judgingRepository.findByCategoryNameOrderByScoreDesc(categoryName);

        List<OpenCategoryWinnerResponse> winners = new ArrayList<>();
        int placeCounter = 1;

        for (OpenJudging j : judgings) {
            if (winners.size() >= limit) break;

            OpenPhoto photo = j.getPhoto(); // Make sure getPhoto() returns OpenPhoto
            OpenUser photographer = photo.getOpenUser();
            Category_Open category = photo.getCategory();

            winners.add(new OpenCategoryWinnerResponse(
                    photographer.getPhotographerId(),
                    photographer.getName(),
                    category != null ? category.getCategoryName() : "Uncategorized",
                    j.getScore(),
                    getOpenPlace(placeCounter++)
            ));
        }
        return winners;
    }

    // Utility to get 1st, 2nd, 3rd...
    private String getOpenPlace(int count) {
        switch (count) {
            case 1: return "1st";
            case 2: return "2nd";
            case 3: return "3rd";
            case 4: return "4th";
            case 5: return "5th";
            default: return count + "th";
        }
    }


}