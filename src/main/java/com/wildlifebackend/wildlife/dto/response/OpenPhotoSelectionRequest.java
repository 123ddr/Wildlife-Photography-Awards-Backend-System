package com.wildlifebackend.wildlife.dto.response;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenPhotoSelectionRequest {
    private Long judgeId;
    private Long photoId;
    private Long categoryId;
    private Boolean isSelected;
}