package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.dto.response.StudentSubmissionDTO;
import com.wildlifebackend.wildlife.entitiy.SchoolSubmission;
import com.wildlifebackend.wildlife.repository.SchoolSubmissionRepositry;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class SchoolSubmissionService {

    private final SchoolSubmissionRepositry schoolSunmissionRepositry;
    private final Path uploadDir= Paths.get("upload");

    public SchoolSubmissionService(SchoolSubmissionRepositry schoolSunmissionRepositry){
        this.schoolSunmissionRepositry=schoolSunmissionRepositry;

        try{
            Files.createDirectories(uploadDir);
        }catch (Exception e){
            throw new RuntimeException("Could not create upload directory");
        }
    }

    public SchoolSubmission saveSchoolSubmission(StudentSubmissionDTO studentSubmissionDTO){
        SchoolSubmission submission=new SchoolSubmission();

        submission.setSubmissionId(studentSubmissionDTO.getSubmissionId());
        submission.setStudentName(studentSubmissionDTO.getStudentName());
        submission.setDateOfBirth(studentSubmissionDTO.getDateOfBirth());
        submission.setContactNo(studentSubmissionDTO.getContactNo());
        submission.setEntryCategories(studentSubmissionDTO.getEntryCategories());
        submission.setEntryTitle(studentSubmissionDTO.getEntryTitle());
        submission.setDateOfPhotograph(studentSubmissionDTO.getDateOfPhotograph());
        submission.setTechnicalInformation(studentSubmissionDTO.getTechnicalInformation());
        submission.setEntryDescription(studentSubmissionDTO.getEntryDescription());


        if (studentSubmissionDTO.getRawFile() != null && !studentSubmissionDTO.getRawFile().isEmpty()) {
            try {
                String filename = studentSubmissionDTO.getRawFile().getOriginalFilename();
                Path targetLocation = uploadDir.resolve(filename);
                Files.copy(studentSubmissionDTO.getRawFile().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                submission.setRawFilePath(targetLocation.toString());
            } catch (Exception e) {
                throw new RuntimeException("File upload failed", e);
            }
        }

        return schoolSunmissionRepositry.save(submission);
    }

    }

