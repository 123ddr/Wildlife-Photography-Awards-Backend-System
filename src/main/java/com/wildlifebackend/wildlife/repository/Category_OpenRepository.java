package com.wildlifebackend.wildlife.repository;


import com.wildlifebackend.wildlife.entitiy.Category_Open;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Category_OpenRepository extends JpaRepository<Category_Open, Long> {
    // Basic find methods
    Optional<Category_Open> findByName(String name);
}
