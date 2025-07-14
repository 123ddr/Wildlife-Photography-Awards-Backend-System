package com.wildlifebackend.wildlife.repository;

import com.wildlifebackend.wildlife.entitiy.SchoolJudge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolJudgeRepo extends JpaRepository<SchoolJudge, Long> {
    Optional<SchoolJudge> findByEmail(String email);
}