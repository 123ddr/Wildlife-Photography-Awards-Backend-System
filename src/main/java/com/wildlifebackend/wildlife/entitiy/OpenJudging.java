package com.wildlifebackend.wildlife.entitiy;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "open_judging")
@Getter
@Setter
public class OpenJudging {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long judgingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "judge_id", nullable = false)
    @JsonBackReference
    private OpenJudge judge;

    @Column(name = "photo_id", nullable = false)
    private Long photoId;

    private Double score;

    private String feedback;

    public OpenJudging(OpenJudge judge, Long photoId, Double score, String feedback) {
        this.judge = judge;
        this.photoId = photoId;
        this.score = score;
        this.feedback = feedback;
    }

    public OpenJudging() {

    }
}
