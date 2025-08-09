package com.wildlifebackend.wildlife.repository;

import com.wildlifebackend.wildlife.entitiy.Category_School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Category_SchoolRepository extends JpaRepository<Category_School, Long> {

    Optional<Category_School> findByName(String name);


}
