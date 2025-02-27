package com.wildlifebackend.wildlife.entitiy;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "Submission")
@Getter
@Setter
public class SchoolSubmission {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long SubmissionId;
    private String StudentName;
    private Date dateOfBirth;
    private String contactNo;

    @ElementCollection
    @CollectionTable(name = "entry categories", joinColumns=@JoinColumn(name = "SubmissionId"))
    @Column(name = "category")
    private Set<String> entryCategories;
    private String entryTitle;
    private Date dateOfPhotograph;
    private String technicalInformation;
    private String rawFilePath;
    private String EntryDescription;
    private String entryUploadPath;


}
