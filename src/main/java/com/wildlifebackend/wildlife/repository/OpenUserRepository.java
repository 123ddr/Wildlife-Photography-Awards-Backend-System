package com.wildlifebackend.wildlife.repository;


import com.wildlifebackend.wildlife.entitiy.OpenUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OpenUserRepository extends JpaRepository<OpenUser, Long> {

    Optional<OpenUser> findByEmail(String email);
    Optional<OpenUser> findByNic(String nic);

}
