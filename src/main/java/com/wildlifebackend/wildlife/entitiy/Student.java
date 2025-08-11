package com.wildlifebackend.wildlife.entitiy;

import com.wildlifebackend.wildlife.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Setter
@Getter
@Table(name = "students")
public class Student extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudentPhoto> photos = new ArrayList<>();

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @NotBlank(message = "Guardian's name is required")
    @Column(name = "guardian_name")
    @Size(max = 100, message = "Guardian's name must be less than 100 characters")
    private String guardianName;

    @NotBlank(message = "Contact number is required")
    @Column(name = "contact_number")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Contact number must be 10-15 digits")
    private String contactNumber;

    @NotBlank(message = "School name is required")
    @Column(name = "school_name")
    @Size(max = 200, message = "School name must be less than 200 characters")
    private String schoolName;

    @NotBlank(message = "School address is required")
    @Column(name = "school_address")
    @Size(max = 500, message = "School address must be less than 500 characters")
    private String schoolAddress;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String schoolEmail;

    @NotBlank(message = "Password is required")
    private String password;

    @Transient
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    @OneToMany(mappedBy = "photographer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SchoolSubmission> submissions = new HashSet<>();

    @Column(name = "is_active")
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "photographer_id", unique = true, updatable = false)
    private String photographerId;
}