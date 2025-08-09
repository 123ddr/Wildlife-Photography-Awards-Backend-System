package com.wildlifebackend.wildlife.repository;

import com.wildlifebackend.wildlife.entitiy.StudentPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentPhotoRepo extends JpaRepository<StudentPhoto,Long> {
    List<StudentPhoto> findByStudentId(Long studentId);

    // Find photos by category
    @Query("SELECT p FROM StudentPhoto p WHERE p.category.categoryId = :categoryId")
    List<StudentPhoto> findByCategoryId(@Param("categoryId") Long categoryId);


    // Find photos by category name
    @Query("SELECT p FROM StudentPhoto p WHERE p.category.name = :categoryName")
    List<StudentPhoto> findByCategoryName(@Param("categoryName") String categoryName);
}
