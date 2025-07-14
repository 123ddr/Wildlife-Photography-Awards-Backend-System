package com.wildlifebackend.wildlife.repository;

import com.wildlifebackend.wildlife.entitiy.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepo extends JpaRepository<UserRoleEntity, Integer> {
}
