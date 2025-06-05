package com.wildlifebackend.wildlife.dto.response;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jdk.jshell.Snippet;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;

@Data
public class OpenSubmissionDTO {

    @NotEmpty(message = "At least one photographer ID is required")
    private Set<Long> photographerIds;

    private Set<String> entryCategories;
    @NotBlank
    private String entryTitle;
    @NotNull
    private LocalDate dateOfPhotograph;
    @NotBlank
    private String technicalInfo;

    private MultipartFile rawFile;
    private String entryDescription;

}
