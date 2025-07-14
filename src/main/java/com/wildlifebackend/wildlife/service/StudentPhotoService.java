package com.wildlifebackend.wildlife.service;

import com.wildlifebackend.wildlife.entitiy.Student;
import com.wildlifebackend.wildlife.entitiy.StudentPhoto;
import com.wildlifebackend.wildlife.exception.ResourceNotFoundException;
import com.wildlifebackend.wildlife.repository.StudentPhotoRepo;
import com.wildlifebackend.wildlife.repository.StudentRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class StudentPhotoService {

    @Autowired
    private StudentPhotoRepo studentPhotoRepo;

    @Autowired
    private StudentRepositary studentRepositary;


    public StudentPhoto uploadPhoto(Long studentId, MultipartFile file,String title,String description) throws IOException {

        Student student=studentRepositary.findById(studentId)
                .orElseThrow(()->new ResourceNotFoundException("Student not found"));

        StudentPhoto studentPhoto=new StudentPhoto();
        studentPhoto.setTitle(title);
        studentPhoto.setDescription(description);
        studentPhoto.setFileData(file.getBytes());
        studentPhoto.setStudent(student);

        return studentPhotoRepo.save(studentPhoto);



    }

    public StudentPhoto getPhotoById(Long photoId){
        return studentPhotoRepo.findById(photoId)
                .orElseThrow(()->new ResourceNotFoundException("photo not found"));
    }

    public List<StudentPhoto> getAllPhotosByStudent(Long studentId) {
        return studentPhotoRepo.findByStudentId(studentId);
    }

    public StudentPhoto updatePhotoDetails(Long photoId,String title,String description){
        StudentPhoto studentPhoto=getPhotoById(photoId);
        studentPhoto.setTitle(title);
        studentPhoto.setDescription(description);



        return studentPhotoRepo.save(studentPhoto);
    }

    public void deleteStudentPhoto(Long photoId){
        studentPhotoRepo.deleteById(photoId);
    }

}
