package com.wildlifebackend.wildlife.entitiy;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@Table(name = "open_submissions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OpenSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Formatted photographer ID (e.g., "OpenUser_ID_0001")
    @Column(name = "photographer_id", nullable = false, updatable = false)
    private String photographerId;

    @NotBlank
    @Column(name = "entry_title", nullable = false)
    private String entryTitle;

    @NotNull
    @Column(name = "date_of_photograph", nullable = false)
    private LocalDate dateOfPhotograph;

    @NotBlank
    @Column(name = "technical_info", nullable = false)
    private String technicalInfo;

    @Column(name = "raw_file_path")
    private String rawFilePath;

    @Lob
    @Column(name = "entry_description")
    private String entryDescription;

    @NotBlank
    @Column(name = "email", nullable = false)
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank
    @Column(name = "mobile_number", nullable = false)
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Mobile number should be valid")
    private String mobileNumber;

    // Relationships

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category_Open category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private OpenUser photographer;

    //(only one category allowed)
    @NotBlank(message = "Entry category is required")
    @Column(name = "entry_category")
    private String entryCategory;

    @OneToMany(
            mappedBy = "submission",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<OpenPhoto> photos = new HashSet<>();

    @PrePersist
    private void formatPhotographerId() {
        if (photographer != null && photographerId == null) {
            this.photographerId = String.format("OpenUser_ID_%04d", photographer.getId());
        }
    }

}
