package com.wildlifebackend.wildlife.controller;


import com.wildlifebackend.wildlife.entitiy.Category_Open;
import com.wildlifebackend.wildlife.entitiy.OpenPhoto;
import com.wildlifebackend.wildlife.entitiy.OpenSubmission;
import com.wildlifebackend.wildlife.service.Category_OpenFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories_open")
@CrossOrigin(origins = "*")
public class Category_OpenFilterController {

    private final Category_OpenFilterService filterService;

    @Autowired
    public Category_OpenFilterController(Category_OpenFilterService filterService) {
        this.filterService = filterService;
    }

    // Get all categories (for dropdown/selection)
    @GetMapping
    public ResponseEntity<List<Category_Open>> getAllCategories() {
        List<Category_Open> categories = filterService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Get photos by category ID
    @GetMapping("/{categoryId}/photos")
    public ResponseEntity<List<OpenPhoto>> getPhotosByCategory(@PathVariable Long categoryId) {
        List<OpenPhoto> photos = filterService.getPhotosByCategoryId(categoryId);
        return ResponseEntity.ok(photos);
    }

    // Get submissions by category ID
    @GetMapping("/{categoryId}/submissions")
    public ResponseEntity<List<OpenSubmission>> getSubmissionsByCategory(@PathVariable Long categoryId) {
        List<OpenSubmission> submissions = filterService.getSubmissionsByCategoryId(categoryId);
        return ResponseEntity.ok(submissions);
    }

    // Alternative endpoints using category name instead of ID
    @GetMapping("/by-name/{categoryName}/photos")
    public ResponseEntity<List<OpenPhoto>> getPhotosByCategoryName(@PathVariable String categoryName) {
        List<OpenPhoto> photos = filterService.getPhotosByCategoryName(categoryName);
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/by-name/{categoryName}/submissions")
    public ResponseEntity<List<OpenSubmission>> getSubmissionsByCategoryName(@PathVariable String categoryName) {
        List<OpenSubmission> submissions = filterService.getSubmissionsByCategoryName(categoryName);
        return ResponseEntity.ok(submissions);
    }
}
