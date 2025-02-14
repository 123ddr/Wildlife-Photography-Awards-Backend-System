package com.wildlifebackend.wildlife.dto.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoDTO {

    private Long photoId;
    private String title;
    private String description;
}
