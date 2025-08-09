package com.wildlifebackend.wildlife.entitiy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wildlifebackend.wildlife.entitiy.Category_School;
import com.wildlifebackend.wildlife.entitiy.OpenPhoto;
import com.wildlifebackend.wildlife.entitiy.Student;
import com.wildlifebackend.wildlife.entitiy.StudentPhoto;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Entity
@Data
@Table(name = "school_submissions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SchoolSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "photographer_id", nullable = false, updatable = false)
    private String photographerId;

    @NotBlank(message = "Entry title is required")
    @Column(name = "entry_title", nullable = false)
    private String entryTitle;

    @NotNull(message = "Date of photograph is required")
    @Column(name = "date_of_photograph", nullable = false)
    private LocalDate dateOfPhotograph;

    @NotBlank(message = "Technical information is required")
    @Column(name = "technical_info", nullable = false)
    private String technicalInfo;

    @Column(name = "raw_file_path")
    private String rawFilePath;

    @Lob
    @Column(name = "entry_description")
    private String entryDescription;

    @NotBlank(message = "School email is required")
    @Email(message = "Email should be valid")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Contact number should be valid")
    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;



    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category_School category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Student photographer;

    @NotBlank(message = "Entry category is required")
    @Column(name = "entry_category")
    private String entryCategory;



    @OneToMany(
            mappedBy = "submission",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<StudentPhoto> photos = new HashSet<>();

    @PrePersist
    private void formatPhotographerId() {
        if (photographer != null && photographerId == null) {
            this.photographerId = String.format("Student_ID_%04d", photographer.getId());
        }
    }
}