package com.wildlifebackend.wildlife.repository;


import com.wildlifebackend.wildlife.entitiy.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    List<Photo> findByOwnerId(Long userId);

}
