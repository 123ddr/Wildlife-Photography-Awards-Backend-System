package com.wildlifebackend.wildlife.repository;

import com.wildlifebackend.wildlife.entitiy.SubmissionSchool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<SubmissionSchool,Long> {
}
