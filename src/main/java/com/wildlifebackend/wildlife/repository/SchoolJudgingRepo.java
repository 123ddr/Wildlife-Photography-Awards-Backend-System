package com.wildlifebackend.wildlife.repository;

import com.wildlifebackend.wildlife.entitiy.SchoolJudge;
import com.wildlifebackend.wildlife.entitiy.SchoolJudging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolJudgingRepo extends JpaRepository<SchoolJudging, Long> {
    List<SchoolJudging> findByJudge(SchoolJudge schoolJudge);
    List<SchoolJudging> findByPhotoId(Long photoId);
    Optional<SchoolJudging> findByJudgeAndPhotoId(SchoolJudge schoolJudge, Long photoId);

    @Query("SELECT AVG(j.score) FROM SchoolJudging j WHERE j.photoId = :photoId")
    Optional<Double> findAverageScoreByPhotoId(@Param("photoId") Long photoId);
}