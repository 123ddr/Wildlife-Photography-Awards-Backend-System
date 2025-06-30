package com.wildlifebackend.wildlife.repository;


import com.wildlifebackend.wildlife.entitiy.OpenJudge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OpenJudgeRepo extends JpaRepository<OpenJudge, Long> {
    Optional<OpenJudge> findByEmail(String email);
}
