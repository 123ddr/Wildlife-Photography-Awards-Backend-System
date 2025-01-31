package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.entitiy.Student;

public interface StudentService {

    void registerStudent(Student student);

    Student loginStudent(String email, String password);

}
