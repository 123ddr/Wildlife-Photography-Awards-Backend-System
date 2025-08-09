package com.wildlifebackend.wildlife.dto.response;


import lombok.Data;

@Data
public class JudgingUpdateRequest {
    private Double score;
    private String feedback;
}
