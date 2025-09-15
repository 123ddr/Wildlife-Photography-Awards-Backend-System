package com.wildlifebackend.wildlife.entitiy;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "open_judging_categories")
@Getter
@Setter
public class OpenJudgingCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false, unique = true)
    private String categoryName;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<OpenCategoryPhoto> photos;

    public OpenJudgingCategory() {}

    public OpenJudgingCategory(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
    }
}