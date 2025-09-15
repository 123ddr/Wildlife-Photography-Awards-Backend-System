package com.wildlifebackend.wildlife.dto.response;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolPhotoSelectionRequest {
    private Long judgeId;
    private Long photoId;
    private Long categoryId;
    private Boolean isSelected;
}



//4. Selection Request DTO
//Purpose: Data transfer object for the "MARK" button functionality
//
//Why it's needed:
//
//Standardizes the data structure for photo selection requests
//
//Contains all necessary information: judge ID, photo ID, category ID, and selection status
//
//Ensures clean API communication between frontend and backend
//
//Usage: When judge clicks "MARK" button, this object is sent to the backend.