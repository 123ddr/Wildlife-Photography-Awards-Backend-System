package com.wildlifebackend.wildlife.entitiy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "school_judging")
@Getter
@Setter
public class SchoolJudging {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long judgingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "judge_id", nullable = false)
    @JsonBackReference
    private SchoolJudge judge;

    @Column(name = "photo_id", nullable = false)
    private Long photoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    private StudentPhoto photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, referencedColumnName = "categoryId")
    @JsonIgnore
    private SchoolJudgingCategory category;

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

    public SchoolJudging() {}

    public SchoolJudging(SchoolJudge judge, Long photoId, SchoolJudgingCategory category) {
        this.judge = judge;
        this.photoId = photoId;
        this.category = category;
        this.isSelected = false;
        this.isMarked = false;

    }

    public StudentPhoto getPhoto() {
        return this.photo;
    }
}