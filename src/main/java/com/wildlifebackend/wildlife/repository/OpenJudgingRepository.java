package com.wildlifebackend.wildlife.repository;



import com.wildlifebackend.wildlife.entitiy.OpenJudging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpenJudgingRepository extends JpaRepository<OpenJudging, Long> {

    List<OpenJudging> findByJudgeJudgeIdAndCategoryCategoryIdAndIsSelectedTrue(Long judgeId, Long categoryId);

    Optional<OpenJudging> findByJudgeJudgeIdAndPhotoIdAndCategoryCategoryId(Long judgeId, Long photoId, Long categoryId);

    @Query("SELECT oj FROM OpenJudging oj WHERE oj.judge.judgeId = :judgeId AND oj.category.categoryId = :categoryId AND oj.isSelected = true AND oj.isMarked = false")
    List<OpenJudging> findUnmarkedSelectedPhotos(@Param("judgeId") Long judgeId, @Param("categoryId") Long categoryId);

    List<OpenJudging> findAllByOrderByScoreDesc();

    @Query("SELECT j FROM OpenJudging j WHERE j.category.categoryName = :categoryName ORDER BY j.score DESC")
    List<OpenJudging> findByCategoryNameOrderByScoreDesc(@Param("categoryName") String categoryName);
}