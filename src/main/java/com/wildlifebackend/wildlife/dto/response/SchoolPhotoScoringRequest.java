package com.wildlifebackend.wildlife.dto.response;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolPhotoScoringRequest {
    private Long judgingId;
    private Double creativity;
    private Double composition;
    private Double lighting;
    private Double focus;
    private Double originality;
    private Double technicalQuality;
    private Double impact;
    private Double subjectMatter;
    private String feedback;

    public Double calculateTotalScore() {
        return (creativity + composition + lighting + focus + originality +
                technicalQuality + impact + subjectMatter) / 8.0;
    }
}