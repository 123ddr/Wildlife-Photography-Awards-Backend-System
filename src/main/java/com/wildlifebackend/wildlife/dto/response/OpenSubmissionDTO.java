package com.wildlifebackend.wildlife.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@Data
public class OpenSubmissionDTO {

    @NotNull(message = "Photographer ID is required")
    private Long photographerId;

    @NotBlank(message = "Entry category is required")
    private String entryCategory;  // Single category selection

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

    // Helper method to get formatted photographer ID
    public String getFormattedPhotographerId() {
        return String.format("OpenUser_ID_%04d", photographerId);
    }

}
