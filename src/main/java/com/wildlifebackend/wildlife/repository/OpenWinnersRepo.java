package com.wildlifebackend.wildlife.repository;


import com.wildlifebackend.wildlife.entitiy.OpenWinners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenWinnersRepo extends JpaRepository<OpenWinners, Long> {
    void deleteAllByCategoryName(String categoryName); // Optional: clear old winners per category
}
