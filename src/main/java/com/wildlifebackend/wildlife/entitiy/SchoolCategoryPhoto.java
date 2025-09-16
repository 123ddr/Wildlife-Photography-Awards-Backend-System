package com.wildlifebackend.wildlife.entitiy;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "school-category_photos")
@Getter
@Setter
public class SchoolCategoryPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryPhotoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private SchoolJudgingCategory category;

    @Column(name = "photo_id", nullable = false)
    private Long photoId;

    @Column(nullable = false)
    private Boolean isSelected = false;

    public SchoolCategoryPhoto() {}

    public SchoolCategoryPhoto(SchoolJudgingCategory category, Long photoId) {
        this.category = category;
        this.photoId = photoId;
    }
}








