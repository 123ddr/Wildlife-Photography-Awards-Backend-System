package com.wildlifebackend.wildlife.repository;

import com.wildlifebackend.wildlife.entitiy.StudentPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentPhotoRepo extends JpaRepository<StudentPhoto,Long> {
    List<StudentPhoto> findByStudentId(Long studentId);
}
