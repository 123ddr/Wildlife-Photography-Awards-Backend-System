package com.wildlifebackend.wildlife.repository;


import com.wildlifebackend.wildlife.entitiy.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepositary extends JpaRepository<Student,Long> {

    Optional<Student> findBySchoolEmail(String schoolEmail);
    @Query("SELECT MAX(u.id) FROM Student u")
    Optional<Long> findMaxId();
}
