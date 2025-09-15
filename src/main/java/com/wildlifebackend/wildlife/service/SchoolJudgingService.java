package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.dto.response.*;
import com.wildlifebackend.wildlife.entitiy.*;
import com.wildlifebackend.wildlife.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolJudgingService {

    private final SchoolJudgingRepository judgingRepository;
    private final SchoolJudgeRepo judgeRepository;
    private final SchoolJudgingCategoryRepository categoryRepository;
    private final SchoolWinnersRepo winnersRepository;

    @Transactional
    public SchoolJudging selectPhoto(SchoolPhotoSelectionRequest request) {
        SchoolJudge judge = judgeRepository.findById(request.getJudgeId())
                .orElseThrow(() -> new RuntimeException("Judge not found"));

        SchoolJudgingCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Check if already exists
        SchoolJudging judging = judgingRepository
                .findByJudgeJudgeIdAndPhotoIdAndCategoryCategoryId(
                        request.getJudgeId(), request.getPhotoId(), request.getCategoryId())
                .orElseGet(() -> new SchoolJudging(judge, request.getPhotoId(), category));

        judging.setIsSelected(request.getIsSelected());
        return judgingRepository.save(judging);
    }

    public List<SchoolJudging> getSelectedPhotos(Long judgeId, Long categoryId) {
        return judgingRepository.findByJudgeJudgeIdAndCategoryCategoryIdAndIsSelectedTrue(judgeId, categoryId);
    }

    public List<SchoolJudging> getUnmarkedPhotos(Long judgeId, Long categoryId) {
        return judgingRepository.findUnmarkedSelectedPhotos(judgeId, categoryId);
    }

    @Transactional
    public SchoolJudging scorePhoto(SchoolPhotoScoringRequest request) {
        SchoolJudging judging = judgingRepository.findById(request.getJudgingId())
                .orElseThrow(() -> new RuntimeException("Judging record not found"));

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
    public List<SchoolWinnerResponse> calculateAndSaveWinners() {
        List<SchoolJudging> judgings = judgingRepository.findAllByOrderByScoreDesc();
        List<SchoolWinnerResponse> winners = new ArrayList<>();

        // Clear old winners
        winnersRepository.deleteAll();

        int placeCounter = 1;
        for (SchoolJudging j : judgings) {
            StudentPhoto photo = j.getPhoto();
            Student photographer = photo.getStudent();
            Category_School category = photo.getCategory();

            String place = getPlace(placeCounter);

            SchoolWinnerResponse response = new SchoolWinnerResponse(
                    photographer.getPhotographerId(),
                    photographer.getName(),
                    category != null ? category.getCategoryName() : "Uncategorized",
                    j.getScore(),
                    place
            );

            winners.add(response);

            // Save to DB
            SchoolWinners winnerEntity = new SchoolWinners();
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
    public List<SchoolCategoryWinnerResponse> getTopWinnersByCategory(String categoryName, int limit) {
        // Use the new repository method
        List<SchoolJudging> judgings = judgingRepository.findByCategoryNameOrderByScoreDesc(categoryName);

        List<SchoolCategoryWinnerResponse> winners = new ArrayList<>();
        int placeCounter = 1;

        for (SchoolJudging j : judgings) {
            if (winners.size() >= limit) break;

            StudentPhoto photo = j.getPhoto();
            Student photographer = photo.getStudent();
            Category_School category = photo.getCategory();

            winners.add(new SchoolCategoryWinnerResponse(
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