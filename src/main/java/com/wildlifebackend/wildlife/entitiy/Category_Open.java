package com.wildlifebackend.wildlife.entitiy;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories_open")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category_Open {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(nullable = false, unique = true)
    private String name;


    // Constructor with name parameter
    public Category_Open(String name) {
        this.name = name;
    }

    public Category_Open() {

    }

    public String getCategoryName() {
        return this.name;
    }
}
