package com.wildlifebackend.wildlife.controller;



import com.wildlifebackend.wildlife.service.OpenPhotoService;
import com.wildlifebackend.wildlife.service.serviceImpl.OpenUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api/openphotos")
@CrossOrigin(origins = "*")
public class OpenPhotoController {

    @Autowired
    private OpenPhotoService openPhotoService;

    @Autowired
    private OpenUserServiceImpl openUserService;



}
