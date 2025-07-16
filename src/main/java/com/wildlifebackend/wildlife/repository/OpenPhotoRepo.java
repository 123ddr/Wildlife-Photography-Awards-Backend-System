package com.wildlifebackend.wildlife.repository;

import com.wildlifebackend.wildlife.entitiy.OpenPhoto;
import com.wildlifebackend.wildlife.entitiy.StudentPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpenPhotoRepo extends JpaRepository<OpenPhoto,Long> {
    List<OpenPhoto> findByOpenUserId(Long openuserId);

    // Find photos by category
    @Query("SELECT p FROM OpenPhoto p WHERE p.category.categoryId = :categoryId")
    List<OpenPhoto> findByCategoryId(@Param("categoryId") Long categoryId);

    // Find photos by category name
    @Query("SELECT p FROM OpenPhoto p WHERE p.category.name = :categoryName")
    List<OpenPhoto> findByCategoryName(@Param("categoryName") String categoryName);

}
