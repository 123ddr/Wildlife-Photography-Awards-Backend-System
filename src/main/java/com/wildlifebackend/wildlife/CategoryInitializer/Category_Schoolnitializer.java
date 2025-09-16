package com.wildlifebackend.wildlife.CategoryInitializer;


import com.wildlifebackend.wildlife.entitiy.Category_School;
import com.wildlifebackend.wildlife.repository.Category_SchoolRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class Category_Schoolnitializer {

    private final Category_SchoolRepository categoryRepository;

    public Category_Schoolnitializer(Category_SchoolRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    public void initCategories() {
        if (categoryRepository.count() == 0) {
            List<Category_School> defaultCategories = Arrays.asList(
                    new Category_School("Animal Behaviours"),
                    new Category_School("Animal Portraits"),
                    new Category_School("Natural Habitats"),
                    new Category_School("Urban Wildlife"),
                    new Category_School("Wild Flora")
            );
            categoryRepository.saveAll(defaultCategories);
            log.info("Initialized 5 default categories");
        }
    }
}
