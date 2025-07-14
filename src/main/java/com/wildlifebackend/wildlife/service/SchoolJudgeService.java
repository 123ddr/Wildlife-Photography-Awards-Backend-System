package com.wildlifebackend.wildlife.service;

import com.wildlifebackend.wildlife.entitiy.SchoolJudge;
import com.wildlifebackend.wildlife.repository.SchoolJudgeRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SchoolJudgeService {

    private final SchoolJudgeRepo judgeRepository;

    public SchoolJudgeService(SchoolJudgeRepo judgeRepository) {
        this.judgeRepository = judgeRepository;
    }

    public SchoolJudge createJudge(String name, String email) {
        SchoolJudge judge = new SchoolJudge(name, email);
        return judgeRepository.save(judge);
    }

    public List<SchoolJudge> getAllJudges() {
        return judgeRepository.findAll();
    }

    public Optional<SchoolJudge> getJudgeById(Long judgeId) {
        return judgeRepository.findById(judgeId);
    }

    public SchoolJudge updateJudge(Long judgeId, String newName, String newEmail) {
        return judgeRepository.findById(judgeId)
                .map(judge -> {
                    if (newName != null) {
                        judge.setName(newName);
                    }
                    if (newEmail != null) {
                        judge.setEmail(newEmail);
                    }
                    return judgeRepository.save(judge);
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