package com.wildlifebackend.wildlife.controller;



import com.wildlifebackend.wildlife.service.OpenPhotoService;
import com.wildlifebackend.wildlife.service.StudentPhotoService;
import com.wildlifebackend.wildlife.service.serviceImpl.OpenUserServiceImpl;
import com.wildlifebackend.wildlife.service.serviceImpl.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api/openphotos")
@CrossOrigin(origins = "*")
public class StudentPhotoController {

    @Autowired
    private StudentPhotoService studentPhotoService;

    @Autowired
    private StudentServiceImpl studentService;



}
