package com.wildlifebackend.wildlife.repository;

import com.wildlifebackend.wildlife.entitiy.SchoolJudgingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolJudgingCategoryRepository extends JpaRepository<SchoolJudgingCategory, Long> {
    SchoolJudgingCategory findByCategoryName(String categoryName);
}