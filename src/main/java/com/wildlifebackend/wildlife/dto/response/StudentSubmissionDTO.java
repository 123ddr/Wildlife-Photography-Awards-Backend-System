package com.wildlifebackend.wildlife.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
public class StudentSubmissionDTO {
    @NotEmpty(message = "At least one photographer ID is required")
    private Set<Long> photographerIds;

    private Set<String> entryCategories;

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

}
