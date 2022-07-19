package com.financemanager.service;

import java.util.List;

import com.financemanager.dto.CategoryDTO;

import org.springframework.stereotype.Component;

@Component
public interface CategoryService {
    CategoryDTO save(CategoryDTO categoryDTO);

    CategoryDTO update(CategoryDTO categoryDTO, Integer id);

    void delete(Integer id);

    List<CategoryDTO> findAll();

    CategoryDTO findById(Integer id);
    
    List<CategoryDTO> findByUserId(Integer userId);

}
