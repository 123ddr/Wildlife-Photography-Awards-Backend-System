package com.wildlifebackend.wildlife.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
public class StudentSubmissionDTO {
    @NotEmpty(message = "At least one photographer ID is required")
    private Long photographerId;

    private String entryCategory;

    @NotBlank(message = "Entry title is required")
    private String entryTitle;

    @NotNull(message = "Date of photograph is required")
    private LocalDate dateOfPhotograph;

    @NotBlank(message = "Technical info is required")
    private String technicalInfo;

    @JsonIgnore
    private MultipartFile rawFile; // For file upload

    private String rawFilePath;     // For storing the path after upload

    private String entryDescription;

    private Long categoryId;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Mobile number should be valid (10-15 digits with optional + prefix)")
    private String mobileNumber;


    public String getFormattedPhotographerId() {
        return String.format("Student_ID_%04d", photographerId);
    }

}
