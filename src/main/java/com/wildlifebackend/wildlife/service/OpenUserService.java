package com.wildlifebackend.wildlife.service;

import com.wildlifebackend.wildlife.entitiy.OpenUser;

public interface OpenUserService {
    void registerUser(OpenUser openuser);

    OpenUser loginUser(String email, String password);
}
