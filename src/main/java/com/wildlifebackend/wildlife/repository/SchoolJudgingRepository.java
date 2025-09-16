package com.wildlifebackend.wildlife.repository;



import com.wildlifebackend.wildlife.entitiy.SchoolJudging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolJudgingRepository extends JpaRepository<SchoolJudging, Long> {

    List<SchoolJudging> findByJudgeJudgeIdAndCategoryCategoryIdAndIsSelectedTrue(Long judgeId, Long categoryId);

    Optional<SchoolJudging> findByJudgeJudgeIdAndPhotoIdAndCategoryCategoryId(
            Long judgeId, Long photoId, Long categoryId);

    @Query("SELECT sj FROM SchoolJudging sj WHERE sj.judge.judgeId = :judgeId AND sj.category.categoryId = :categoryId AND sj.isSelected = true AND sj.isMarked = false")
    List<SchoolJudging> findUnmarkedSelectedPhotos(@Param("judgeId") Long judgeId, @Param("categoryId") Long categoryId);

    List<SchoolJudging> findAllByOrderByScoreDesc();

    @Query("SELECT j FROM SchoolJudging j WHERE j.category.categoryName = :categoryName ORDER BY j.score DESC")
    List<SchoolJudging> findByCategoryNameOrderByScoreDesc(@Param("categoryName") String categoryName);
}