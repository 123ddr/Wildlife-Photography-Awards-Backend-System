package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.entitiy.OpenJudge;
import com.wildlifebackend.wildlife.repository.OpenJudgeRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OpenJudgeService {

    private final OpenJudgeRepo judgeRepository;

    public OpenJudgeService(OpenJudgeRepo judgeRepository) {
        this.judgeRepository = judgeRepository;
    }

    public OpenJudge createJudge(String name, String email) {
        OpenJudge judge = new OpenJudge(name, email);
        return judgeRepository.save(judge);
    }

    public List<OpenJudge> getAllJudges() {
        return judgeRepository.findAll();
    }

    public Optional<OpenJudge> getJudgeById(Long judgeId) {
        return judgeRepository.findById(judgeId);
    }

    public OpenJudge updateJudge(Long judgeId, String newName, String newEmail) {
        return judgeRepository.findById(judgeId)
                .map(judge -> {
                    if (newName != null) {
                        judge.setName(newName);
                    }
                    if (newEmail != null) {
                        judge.setEmail(newEmail);
                    }
                    return judgeRepository.save(judge); // Dirty checking will auto-update
                })
                .orElseThrow(() -> new RuntimeException("Judge not found with id: " + judgeId));
    }

    public void deleteJudge(Long judgeId) {
        if (judgeRepository.existsById(judgeId)) {
            judgeRepository.deleteById(judgeId);
        } else {
            throw new RuntimeException("Judge not found with id: " + judgeId);
        }
    }
}
