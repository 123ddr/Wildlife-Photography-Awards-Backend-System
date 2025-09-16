package com.wildlifebackend.wildlife.service;

import com.wildlifebackend.wildlife.entitiy.Category_School;
import com.wildlifebackend.wildlife.entitiy.StudentPhoto;
import com.wildlifebackend.wildlife.repository.Category_SchoolRepository;
import com.wildlifebackend.wildlife.repository.StudentPhotoRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("schoolCategoryFilterService")  // Unique name
@Transactional
public class Category_SchoolFilterService {

    private final StudentPhotoRepo photoRepository;
    private final Category_SchoolRepository categoryRepository;

    @Autowired
    public Category_SchoolFilterService(StudentPhotoRepo photoRepository,
                                        Category_SchoolRepository categoryRepository) {
        this.photoRepository = photoRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<StudentPhoto> getPhotosByCategoryId(Long categoryId) {
        return photoRepository.findByCategoryId(categoryId);
    }

    public List<StudentPhoto> getPhotosByCategoryName(String categoryName) {
        return photoRepository.findByCategoryName(categoryName);
    }

    public List<Category_School> getAllCategories() {
        return categoryRepository.findAll();
    }
}