package com.wildlifebackend.wildlife.repository;



import com.wildlifebackend.wildlife.entitiy.SchoolWinners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolWinnersRepo extends JpaRepository<SchoolWinners, Long> {
}
