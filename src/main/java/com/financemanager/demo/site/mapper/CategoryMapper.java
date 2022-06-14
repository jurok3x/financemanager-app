package com.financemanager.demo.site.mapper;

import com.financemanager.demo.site.dto.CategoryDTO;
import com.financemanager.demo.site.entity.Category;

import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    
    public CategoryDTO toCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    public Category toCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        return category;
    }
}
