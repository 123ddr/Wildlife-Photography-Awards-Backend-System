package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.entitiy.OpenPhoto;
import com.wildlifebackend.wildlife.entitiy.OpenUser;
import com.wildlifebackend.wildlife.exception.ResourceNotFoundException;
import com.wildlifebackend.wildlife.repository.OpenPhotoRepo;
import com.wildlifebackend.wildlife.repository.OpenUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class OpenPhotoService {

    @Autowired
    private OpenPhotoRepo openPhotoRepo;

    @Autowired
    private OpenUserRepository openuserRepositary;


    public OpenPhoto uploadPhoto(Long openuserId, MultipartFile file, String title, String description) throws IOException {

        OpenUser openuser=openuserRepositary.findById(openuserId)
                .orElseThrow(()->new ResourceNotFoundException("Student not found"));

        OpenPhoto openPhoto=new OpenPhoto();
        openPhoto.setTitle(title);
        openPhoto.setDescription(description);
        openPhoto.setFileData(file.getBytes());
        openPhoto.setOpenuser(openuser);

        return openPhotoRepo.save(openPhoto);
    }

    public OpenPhoto getPhotoById(Long photoId){
        return openPhotoRepo.findById(photoId)
                .orElseThrow(()->new ResourceNotFoundException("photo not found"));
    }

    public List<OpenPhoto> getAllPhotosByStudent(Long openuserId) {
        return openPhotoRepo.findByOpenUserId(openuserId);
    }

    public OpenPhoto updatePhotoDetails(Long photoId,String title,String description){
        OpenPhoto openPhoto=getPhotoById(photoId);
        openPhoto.setTitle(title);
        openPhoto.setDescription(description);

        return openPhotoRepo.save(openPhoto);
    }

    public void deleteStudentPhoto(Long photoId){
        openPhotoRepo.deleteById(photoId);
    }

}
