package com.wildlifebackend.wildlife.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Set;

@Data
public class StudentSubmissionDTO {
    private Long SubmissionId;
    private String StudentName;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String contactNo;
    private Set<String> entryCategories;
    private String entryTitle;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfPhotograph;
    private String technicalInformation;
    private MultipartFile rawFile;
    private String entryDescription;

}
