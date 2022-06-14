package com.financemanager.demo.site.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.financemanager.demo.site.dto.CategoryDTO;
import com.financemanager.demo.site.entity.payload.SaveCategoryRequest;
import com.financemanager.demo.site.entity.projects.ProjectCategoryAndCost;
import com.financemanager.demo.site.entity.projects.ProjectCategoryAndCount;

@Component
public interface CategoryService {
	CategoryDTO save(SaveCategoryRequest request);
	
	CategoryDTO update(SaveCategoryRequest request, Integer id);

    void delete(Integer id);

    List<CategoryDTO> findAll();
    
    Optional<CategoryDTO> findById(Integer id);
    
    List<ProjectCategoryAndCost> getCategoriesAndCost(Optional<Integer> year, Optional<Integer> month);
    
    List<ProjectCategoryAndCount> getCategoriesAndCount(Optional<Integer> year, Optional<Integer> month);
}
