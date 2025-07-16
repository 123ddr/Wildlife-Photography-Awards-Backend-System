package com.wildlifebackend.wildlife.repository;


import com.wildlifebackend.wildlife.entitiy.OpenSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpenSubmissionRepository extends JpaRepository<OpenSubmission, Long> {

    // Find submissions by category
    @Query("SELECT s FROM OpenSubmission s WHERE s.category.categoryId = :categoryId")
    List<OpenSubmission> findByCategoryId(@Param("categoryId") Long categoryId);

    // Find submissions by category name
    @Query("SELECT s FROM OpenSubmission s WHERE s.category.name = :categoryName")
    List<OpenSubmission> findByCategoryName(@Param("categoryName") String categoryName);
}
