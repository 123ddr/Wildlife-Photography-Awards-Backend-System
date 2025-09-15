package com.wildlifebackend.wildlife.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SchoolCategoryWinnerResponse {
    private String photographerId;
    private String photographerName;
    private String categoryName;
    private Double score;
    private String place;
}
