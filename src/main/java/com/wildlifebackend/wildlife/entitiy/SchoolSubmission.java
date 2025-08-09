package com.wildlifebackend.wildlife.entitiy;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SchoolSubmission")
@Getter
@Setter
public class SchoolSubmission {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "school_submissions",
            joinColumns = @JoinColumn(name = "submission_id"),
            inverseJoinColumns = @JoinColumn(name = "school_id")
    )
    @JsonIgnore
    private Set<Student> photographers = new HashSet<>();
    //OpenSubmission: Represents a photo submission entry.

    @ElementCollection
    @CollectionTable(name = "entry_categories_school", joinColumns = @JoinColumn(name = "submission_id_school"))
    @Column(name = "category")
    private Set<String> entryCategories;

    @NotBlank
    private String entryTitle;

    @NotNull
    private LocalDate dateOfPhotograph;

    @NotBlank
    private String technicalInfo;

    private String rawFilePath;

    @Lob
    private String entryDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category_School category;

}