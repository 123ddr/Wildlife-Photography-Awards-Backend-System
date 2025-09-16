package com.wildlifebackend.wildlife.CategoryInitializer;


import com.wildlifebackend.wildlife.entitiy.Category_Open;
import com.wildlifebackend.wildlife.repository.Category_OpenRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class Category_OpenInitializer {
    private final Category_OpenRepository categoryRepository;

    public Category_OpenInitializer(Category_OpenRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    public void initCategories() {
        if (categoryRepository.count() == 0) {
            List<Category_Open> defaultCategories = Arrays.asList(
                    new Category_Open("Animal Behaviours"),
                    new Category_Open("Animal Portraits"),
                    new Category_Open("Natural Habitats"),
                    new Category_Open("Urban Wildlife"),
                    new Category_Open("Wild Flora")
            );
            categoryRepository.saveAll(defaultCategories);
            log.info("Initialized 5 default categories");
        }
    }
}
