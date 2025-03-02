package com.wildlifebackend.wildlife.entitiy;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class StudentPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    private String title;
    private String description;
    private LocalDate uploadDateTime;

    @Lob
    private byte[] fileData;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;
}
