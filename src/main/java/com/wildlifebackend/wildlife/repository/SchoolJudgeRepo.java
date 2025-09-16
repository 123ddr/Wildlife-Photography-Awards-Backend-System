package com.wildlifebackend.wildlife.repository;

import com.wildlifebackend.wildlife.entitiy.SchoolJudge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface SchoolJudgeRepo extends JpaRepository<SchoolJudge, Long> {
}