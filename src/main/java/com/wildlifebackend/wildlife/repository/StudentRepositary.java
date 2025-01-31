package com.wildlifebackend.wildlife.repository;

import com.wildlifebackend.wildlife.entitiy.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface StudentRepositary extends JpaRepository<Student,Long> {

    Optional<Student> findByEmail(String email);
    Optional<Student> findByPassword(String password);

}
