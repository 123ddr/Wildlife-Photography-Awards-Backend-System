package com.wildlifebackend.wildlife.entitiy;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "school_judging_categories")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SchoolJudgingCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false, unique = true)
    private String categoryName;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<SchoolCategoryPhoto> photos;

    public SchoolJudgingCategory() {}

    public SchoolJudgingCategory(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
    }
}

