package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.entitiy.OpenJudge;
import com.wildlifebackend.wildlife.entitiy.OpenJudging;
import com.wildlifebackend.wildlife.repository.OpenJudgingRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OpenJudgingService {

    private final OpenJudgingRepo judgingRepository;
    private final OpenJudgeService judgeService;

    public OpenJudgingService(OpenJudgingRepo judgingRepository, OpenJudgeService judgeService) {
        this.judgingRepository = judgingRepository;
        this.judgeService = judgeService;
    }

    public OpenJudging createJudging(Long judgeId, Long photoId, Double score, String feedback) {
        OpenJudge judge = judgeService.getJudgeById(judgeId)
                .orElseThrow(() -> new RuntimeException("Judge not found"));

        // Check if judge already judged this photo
        judgingRepository.findByJudgeAndPhotoId(judge, photoId)
                .ifPresent(j -> {
                    throw new RuntimeException("Judge already judged this photo");
                });

        OpenJudging judging = new OpenJudging(judge, photoId, score, feedback);
        return judgingRepository.save(judging);
    }

    public List<OpenJudging> getJudgingsByPhotoId(Long photoId) {
        return judgingRepository.findByPhotoId(photoId);
    }

    public List<OpenJudging> getJudgingsByJudgeId(Long judgeId) {
        OpenJudge judge = judgeService.getJudgeById(judgeId)
                .orElseThrow(() -> new RuntimeException("Judge not found"));
        return judgingRepository.findByJudge(judge);
    }

    public Optional<Double> getAverageScoreForPhoto(Long photoId) {
        return judgingRepository.findAverageScoreByPhotoId(photoId);
    }

    public OpenJudging updateJudging(Long judgingId, Double newScore, String newFeedback) {
        return judgingRepository.findById(judgingId)
                .map(judging -> {
                    if (newScore != null) {
                        judging.setScore(newScore);
                    }
                    if (newFeedback != null) {
                        judging.setFeedback(newFeedback);
                    }
                    return judgingRepository.save(judging);
                })
                .orElseThrow(() -> new RuntimeException("Judging not found with id: " + judgingId));
    }

    public void deleteJudging(Long judgingId) {
        if (!judgingRepository.existsById(judgingId)) {
            throw new RuntimeException("Judging not found with id: " + judgingId);
        }
        judgingRepository.deleteById(judgingId);
    }

}
