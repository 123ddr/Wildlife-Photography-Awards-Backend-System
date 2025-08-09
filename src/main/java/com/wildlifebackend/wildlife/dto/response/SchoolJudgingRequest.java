package com.wildlifebackend.wildlife.dto.response;


import lombok.Data;

@Data
public class SchoolJudgingRequest {
    private Long judgeId;
    private Long photoId;
    private Double score;
    private String feedback;
}
