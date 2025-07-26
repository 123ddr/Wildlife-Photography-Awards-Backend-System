package com.wildlifebackend.wildlife.repository;

import com.wildlifebackend.wildlife.entitiy.SchoolSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SchoolSubmissionRepositry extends JpaRepository<SchoolSubmission,Long> {

    // Find submissions by category
    @Query("SELECT s FROM SchoolSubmission s WHERE s.category.categoryId = :categoryId")
    List<SchoolSubmission> findByCategoryId(@Param("categoryId") Long categoryId);

    // Find submissions by category name
    @Query("SELECT s FROM SchoolSubmission s WHERE s.category.name = :categoryName")
    List<SchoolSubmission> findByCategoryName(@Param("categoryName") String categoryName);
}
