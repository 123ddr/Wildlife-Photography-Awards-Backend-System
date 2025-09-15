package com.wildlifebackend.wildlife.repository;


import com.wildlifebackend.wildlife.entitiy.OpenJudgingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenJudgingCategoryRepository extends JpaRepository<OpenJudgingCategory, Long> {
    OpenJudgingCategory findByCategoryName(String categoryName);
}