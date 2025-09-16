package com.wildlifebackend.wildlife.controller;

import com.wildlifebackend.wildlife.entitiy.Category_School;
import com.wildlifebackend.wildlife.entitiy.StudentPhoto;
import com.wildlifebackend.wildlife.service.Category_SchoolFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories_school")
@CrossOrigin(origins = "*")
public class Category_SchoolFilterController {

    private final Category_SchoolFilterService filterService;

    @Autowired
    public Category_SchoolFilterController(Category_SchoolFilterService filterService) {
        this.filterService = filterService;
    }

    // Get all school categories
    @GetMapping
    public ResponseEntity<List<Category_School>> getAllCategories() {
        List<Category_School> categories = filterService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Get school photos by category ID
    @GetMapping("/{categoryId}/photos")
    public ResponseEntity<List<StudentPhoto>> getPhotosByCategory(@PathVariable Long categoryId) {
        List<StudentPhoto> photos = filterService.getPhotosByCategoryId(categoryId);
        return ResponseEntity.ok(photos);
    }

    // Alternative endpoints using category name
    @GetMapping("/by-name/{categoryName}/photos")
    public ResponseEntity<List<StudentPhoto>> getPhotosByCategoryName(@PathVariable String categoryName) {
        List<StudentPhoto> photos = filterService.getPhotosByCategoryName(categoryName);
        return ResponseEntity.ok(photos);
    }

}