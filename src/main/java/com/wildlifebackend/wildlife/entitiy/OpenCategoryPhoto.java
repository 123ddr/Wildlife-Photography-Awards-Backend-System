package com.wildlifebackend.wildlife.entitiy;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "open_category_photos")
@Getter
@Setter
public class OpenCategoryPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryPhotoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private OpenJudgingCategory category;

    @Column(name = "photo_id", nullable = false)
    private Long photoId;

    @Column(nullable = false)
    private Boolean isSelected = false;

    public OpenCategoryPhoto() {}

    public OpenCategoryPhoto(OpenJudgingCategory category, Long photoId) {
        this.category = category;
        this.photoId = photoId;
    }
}