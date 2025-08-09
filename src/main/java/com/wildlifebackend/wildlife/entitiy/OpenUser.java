package com.wildlifebackend.wildlife.entitiy;


import com.wildlifebackend.wildlife.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "open_users")
@Getter
@Setter
public class OpenUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "openUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OpenPhoto> photos = new ArrayList<>();

    @NotBlank(message = "Name is required")
    private String name;

//    @NotBlank(message = "Last name is required")
//    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;

    @NotBlank
    @Column(name = "mobile_number", nullable = false)
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Mobile number should be valid")
    private String mobileNumber;

    @NotBlank(message = "NIC is required")
    @Column(unique = true)
    private String nic;

//    @NotNull(message = "Date of birth is required")
//    private LocalDate dateOfBirth;

    @NotBlank(message = "Password is required")
    private String password;

    @Transient
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    @Column
    private Boolean isActive;

    @OneToMany(mappedBy = "photographer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpenSubmission> submissions = new ArrayList<>();
    //OpenUser: Represents a user or photographer.

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
