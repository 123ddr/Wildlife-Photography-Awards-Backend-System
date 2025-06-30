package com.wildlifebackend.wildlife.repository;


import com.wildlifebackend.wildlife.entitiy.OpenJudge;
import com.wildlifebackend.wildlife.entitiy.OpenJudging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpenJudgingRepo extends JpaRepository<OpenJudging, Long> {
    List<OpenJudging> findByJudge(OpenJudge openjudge);
    List<OpenJudging> findByPhotoId(Long photoId);
    Optional<OpenJudging> findByJudgeAndPhotoId(OpenJudge openjudge, Long photoId);

    @Query("SELECT AVG(j.score) FROM OpenJudging j WHERE j.photoId = :photoId")
    Optional<Double> findAverageScoreByPhotoId(@Param("photoId") Long photoId);
}
