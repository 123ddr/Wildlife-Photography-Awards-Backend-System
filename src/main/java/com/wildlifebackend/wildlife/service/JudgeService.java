package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.entitiy.Judge;

public interface JudgeService {
    void registerJudge(Judge judge);
    Judge loginJudge(String email, String password);
}

