package com.wildlifebackend.wildlife.repository;


import com.wildlifebackend.wildlife.entitiy.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {


}
