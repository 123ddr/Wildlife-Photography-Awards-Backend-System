package com.wildlifebackend.wildlife.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponse {
    private String token;
    private Integer expireIn;
    private String status;
    private String message;
}
