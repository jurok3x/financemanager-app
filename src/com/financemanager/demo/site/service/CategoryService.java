package com.financemanager.demo.site.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.financemanager.demo.site.entity.Category;
import com.financemanager.demo.site.entity.projects.ProjectCategoryAndCost;
import com.financemanager.demo.site.entity.projects.ProjectCategoryAndCount;

@Component
public interface CategoryService {
	Category saveCategory(Category category);

    void deleteCategory(Integer catId);

    List<Category> findAll();
    
    Optional<Category> findById(Integer catId);
    
    List<ProjectCategoryAndCost> getCategoriesAndCost(Optional<String> year, Optional<String> month);
    
    List<ProjectCategoryAndCount> getCategoriesAndCount(Optional<String> year, Optional<String> month);
}
