package com.wildlifebackend.wildlife.entitiy;

import com.wildlifebackend.wildlife.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Entity
@Setter
@Getter
@Table(name = "students")
public class Student extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "school name is required")
    @Column(name = "School_name")
    private String schoolName;

    @NotBlank(message = "District is required")
    private String District;

    @NotBlank(message = "Zone is required")
    private String Zone;

    @Email(message = "Invalid school email format")
    @NotBlank(message = "School email is required")
    @Column(name = "school_email", unique = true)
    private String schoolEmail;

    @NotBlank(message = "Password is required")
    private String Password;

    @Transient
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;


    @ManyToMany(mappedBy = "photographers", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<SchoolSubmission> submissions = new HashSet<>();

    @Column(name = "is_active")
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}


