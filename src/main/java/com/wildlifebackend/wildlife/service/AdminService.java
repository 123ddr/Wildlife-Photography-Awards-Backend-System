package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.entitiy.Admin;

public interface AdminService {
    void registerAdmin(Admin admin);
    Admin loginAdmin(String email, String password);
}
