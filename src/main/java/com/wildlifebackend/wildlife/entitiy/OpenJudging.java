package com.wildlifebackend.wildlife.entitiy;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    private OpenPhoto photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private OpenJudgingCategory category;

    private Double score;

    private String feedback;

    // Detailed scoring criteria
    private Double creativity;
    private Double composition;
    private Double lighting;
    private Double focus;
    private Double originality;
    private Double technicalQuality;
    private Double impact;
    private Double subjectMatter;

    @Column(nullable = false)
    private Boolean isMarked = false;

    @Column(nullable = false)
    private Boolean isSelected = false;

    // Constructors
    public OpenJudging() {}

    public OpenJudging(OpenJudge judge, Long photoId, OpenJudgingCategory category) {
        this.judge = judge;
        this.photoId = photoId;
        this.category = category;
    }

    public OpenPhoto getPhoto() {
        return this.photo;
    }
}
