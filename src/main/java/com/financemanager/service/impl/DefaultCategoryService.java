package com.financemanager.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.financemanager.dto.CategoryDTO;
import com.financemanager.exception.ResourceNotFoundException;
import com.financemanager.mapper.CategoryMapper;
import com.financemanager.repository.CategoryRepository;
import com.financemanager.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DefaultCategoryService implements CategoryService {
    private static final String CATEGORY_ID_NOT_FOUND_ERROR = "Category with id %d not found";
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        return categoryMapper.toCategoryDTO(categoryRepository.save(categoryMapper.toCategory(categoryDTO)));
    }

    @Override
    public void delete(Integer id) {
        CategoryDTO categoryDTO = categoryRepository.findById(id).map(categoryMapper::toCategoryDTO).orElseThrow(
                () -> new ResourceNotFoundException(String.format(CATEGORY_ID_NOT_FOUND_ERROR, id)));
        categoryRepository.deleteById(categoryDTO.getId());
    }

    @Override
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream().map(categoryMapper::toCategoryDTO).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO findById(Integer id) {
        return categoryRepository.findById(id).map(categoryMapper::toCategoryDTO).orElseThrow(
                () -> new ResourceNotFoundException(String.format(CATEGORY_ID_NOT_FOUND_ERROR, id)));
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDTO, Integer id) {
        categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(CATEGORY_ID_NOT_FOUND_ERROR, id)));
        return categoryMapper.toCategoryDTO(categoryRepository.save(categoryMapper.toCategory(categoryDTO)));
    }

    @Override
    public List<CategoryDTO> findByUserId(Integer userId) {
        return categoryRepository.findByUserId(userId).stream().map(categoryMapper::toCategoryDTO).collect(Collectors.toList());
    }

}
