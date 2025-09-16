package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.entitiy.Category_Open;
import com.wildlifebackend.wildlife.entitiy.OpenPhoto;
import com.wildlifebackend.wildlife.repository.Category_OpenRepository;
import com.wildlifebackend.wildlife.repository.OpenPhotoRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("openCategoryFilterService")  // Unique name
@Transactional
public class Category_OpenFilterService {

    private final OpenPhotoRepo photoRepository;
    private final Category_OpenRepository categoryRepository;

    @Autowired
    public Category_OpenFilterService(OpenPhotoRepo photoRepository,
                                      Category_OpenRepository categoryRepository) {
        this.photoRepository = photoRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<OpenPhoto> getPhotosByCategoryId(Long categoryId) {
        return photoRepository.findByCategoryId(categoryId);
    }

    public List<OpenPhoto> getPhotosByCategoryName(String categoryName) {
        return photoRepository.findByCategoryName(categoryName);
    }

    public List<Category_Open> getAllCategories() {
        return categoryRepository.findAll();
    }
}
