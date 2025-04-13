package com.wildlifebackend.wildlife.repository;

import com.wildlifebackend.wildlife.entitiy.OpenPhoto;
import com.wildlifebackend.wildlife.entitiy.StudentPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpenPhotoRepo extends JpaRepository<OpenPhoto,Long> {
    List<OpenPhoto> findByOpenUserId(Long openuserId);
}
