package com.wildlifebackend.wildlife.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class TokenResponse {
    private String token;
    private Integer expireIn;
    private String status;
    private String message;
}
