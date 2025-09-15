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








//
//
//
//
//
//2. Category Photo Entity (CategoryPhoto)
//Purpose: To link photos to specific categories for judging
//
//Why it's needed:
//
//Connects photos to the appropriate judging category
//
//Tracks which photos are available for judging in each category
//
//Manages the selection status of photos within categories
//
//Key fields:
//
//categoryPhotoId: Unique identifier
//
//category: Which category this photo belongs to
//
//photoId: Reference to the actual photo (from your existing system)
//
//isSelected: Whether this photo has been marked for judging