package com.wildlifebackend.wildlife.entitiy;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "school_winners")
@Getter
@Setter
public class SchoolWinners {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String photographerId;
    private String photographerName;
    private String categoryName;
    private Double score;
    private String place;
}
