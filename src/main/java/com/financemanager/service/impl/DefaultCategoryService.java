package com.financemanager.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.financemanager.dto.CategoryDTO;
import com.financemanager.exception.ResourceNotFoundException;
import com.financemanager.mapper.CategoryMapper;
import com.financemanager.repository.CategoryRepository;
import com.financemanager.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@PropertySource(value = { "classpath:/messages/category/info.properties" })
public class DefaultCategoryService implements CategoryService {
    
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    
    @Value("${category_id_not_found.error}")
    private String categoryIdNotFoundError;

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        return categoryMapper.toCategoryDTO(categoryRepository.save(categoryMapper.toCategory(categoryDTO)));
    }

    @Override
    public void delete(Integer id) {
        CategoryDTO categoryDTO = categoryRepository.findById(id).map(categoryMapper::toCategoryDTO).orElseThrow(
                () -> new ResourceNotFoundException(String.format(categoryIdNotFoundError, id)));
        categoryRepository.deleteById(categoryDTO.getId());
    }

    @Override
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream().map(categoryMapper::toCategoryDTO).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO findById(Integer id) {
        return categoryRepository.findById(id).map(categoryMapper::toCategoryDTO).orElseThrow(
                () -> new ResourceNotFoundException(String.format(categoryIdNotFoundError, id)));
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDTO, Integer id) {
        categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(categoryIdNotFoundError, id)));
        return categoryMapper.toCategoryDTO(categoryRepository.save(categoryMapper.toCategory(categoryDTO)));
    }

    @Override
    public List<CategoryDTO> findByUserId(Integer userId) {
        return categoryRepository.findByUserId(userId).stream().map(categoryMapper::toCategoryDTO).collect(Collectors.toList());
    }

}
