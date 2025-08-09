package com.wildlifebackend.wildlife.entitiy;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories_school")
@Getter
@Setter
public class Category_School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(nullable = false, unique = true)
    private String name;

    // Constructor with name parameter
    public Category_School(String name) {
        this.name = name;
    }

    // Default constructor
    public Category_School() {
    }
}