package com.wildlifebackend.wildlife.entitiy;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@Table(name = "Open_submissions")
@Getter
@Setter
public class OpenSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long photographerId;

    @ElementCollection
    @CollectionTable(name = "entry_categories", joinColumns = @JoinColumn(name = "submission_id"))
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
}
