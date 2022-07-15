package com.financemanager.service;

import java.util.List;
import java.util.Optional;

import com.financemanager.dto.CategoryDTO;
import com.financemanager.entity.payload.SaveCategoryRequest;

import org.springframework.stereotype.Component;

@Component
public interface CategoryService {
	CategoryDTO save(SaveCategoryRequest request);
	
	CategoryDTO update(SaveCategoryRequest request, Integer id);

    void delete(Integer id);

    List<CategoryDTO> findAll();
    
    Optional<CategoryDTO> findById(Integer id);
    
}
