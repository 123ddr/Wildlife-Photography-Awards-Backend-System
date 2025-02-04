package com.wildlifebackend.wildlife.entitiy;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Submission")
@Getter
@Setter
public class SubmissionSchool {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long SubmissionId;
    private String StudentName;
    private Date dateOfBirth;
    private String contactNo;
    private String entryCategories;
    private String entryTitle;
    private Date dateOfPhotograph;
    private String technicalInformation;
    private String rawFile;
    private String EntryDescription;
    private String entryUploadPath;


}
