package com.wildlifebackend.wildlife.controller;


import com.wildlifebackend.wildlife.entitiy.SubmissionSchool;
import com.wildlifebackend.wildlife.repository.SubmissionRepository;
import com.wildlifebackend.wildlife.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/create")
    public SubmissionSchool createSubmission(
            @RequestParam("studentName") String studentName,
            @RequestParam("dateOfBirth") Date dateOfBirth,
            @RequestParam("contactNo") String contactNo,
            @RequestParam("entryCategories") String entryCategories,
            @RequestParam("entryTitle") String entryTitle,
            @RequestParam("dateOfPhotograph") Date dateOfPhotograph,
            @RequestParam("technicalInformation") String technicalInformation,
            @RequestParam("rawFile") String rawFile,
            @RequestParam("entryDescription") String entryDescription,
            @RequestParam("file") MultipartFile file
    ) throws IOException{
        String filePath=fileUploadService.uploadFile(file);

        SubmissionSchool submissionSchool=new SubmissionSchool();

        submissionSchool.setStudentName(studentName);
        submissionSchool.setDateOfBirth(dateOfBirth);
        submissionSchool.setContactNo(contactNo);
        submissionSchool.setEntryCategories(entryCategories);
        submissionSchool.setEntryTitle(entryTitle);
        submissionSchool.setDateOfPhotograph(dateOfPhotograph);
        submissionSchool.setTechnicalInformation(technicalInformation);
        submissionSchool.setRawFile(rawFile);
        submissionSchool.setEntryDescription(entryDescription);
        submissionSchool.setEntryUploadPath(filePath);

        return submissionRepository.save(submissionSchool);
    }
}
