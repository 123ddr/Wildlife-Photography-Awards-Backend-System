package com.wildlifebackend.wildlife.repository;

import com.wildlifebackend.wildlife.entitiy.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepositary extends JpaRepository<Student,Long> {

    Optional<Student> findBySchoolEmail(String schoolEmail);
    boolean existsBySchoolEmail(String schoolEmail);

}
