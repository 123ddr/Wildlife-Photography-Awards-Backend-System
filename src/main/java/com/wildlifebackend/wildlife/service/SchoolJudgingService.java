package com.wildlifebackend.wildlife.service;

import com.wildlifebackend.wildlife.entitiy.SchoolJudge;
import com.wildlifebackend.wildlife.entitiy.SchoolJudging;
import com.wildlifebackend.wildlife.repository.SchoolJudgingRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SchoolJudgingService {

    private final SchoolJudgingRepo judgingRepository;
    private final SchoolJudgeService judgeService;

    public SchoolJudgingService(SchoolJudgingRepo judgingRepository, SchoolJudgeService judgeService) {
        this.judgingRepository = judgingRepository;
        this.judgeService = judgeService;
    }

    public SchoolJudging createJudging(Long judgeId, Long photoId, Double score, String feedback) {
        SchoolJudge judge = judgeService.getJudgeById(judgeId)
                .orElseThrow(() -> new RuntimeException("Judge not found"));

        // Check if judge already judged this photo
        judgingRepository.findByJudgeAndPhotoId(judge, photoId)
                .ifPresent(j -> {
                    throw new RuntimeException("Judge already judged this photo");
                });

        SchoolJudging judging = new SchoolJudging(judge, photoId, score, feedback);
        return judgingRepository.save(judging);
    }

    public List<SchoolJudging> getJudgingsByPhotoId(Long photoId) {
        return judgingRepository.findByPhotoId(photoId);
    }

    public List<SchoolJudging> getJudgingsByJudgeId(Long judgeId) {
        SchoolJudge judge = judgeService.getJudgeById(judgeId)
                .orElseThrow(() -> new RuntimeException("Judge not found"));
        return judgingRepository.findByJudge(judge);
    }

    public Optional<Double> getAverageScoreForPhoto(Long photoId) {
        return judgingRepository.findAverageScoreByPhotoId(photoId);
    }

    public SchoolJudging updateJudging(Long judgingId, Double newScore, String newFeedback) {
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