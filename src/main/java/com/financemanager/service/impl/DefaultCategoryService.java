package com.financemanager.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.financemanager.dto.CategoryDTO;
import com.financemanager.entity.payload.SaveCategoryRequest;
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
    public CategoryDTO save(SaveCategoryRequest request) {
        CategoryDTO newCategory = new CategoryDTO();
        newCategory.setName(request.getName());
        return categoryMapper.toCategoryDTO(categoryRepository.save(categoryMapper.toCategory(newCategory)));
    }

    @Override
    public void delete(Integer id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream().map(categoryMapper::toCategoryDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<CategoryDTO> findById(Integer id) {
        CategoryDTO categoryDTO = categoryRepository.findById(id).map(categoryMapper::toCategoryDTO).orElseThrow(
                () -> new ResourceNotFoundException(String.format(CATEGORY_ID_NOT_FOUND_ERROR, id)));
        return Optional.ofNullable(categoryDTO);
    }

    @Override
    public CategoryDTO update(SaveCategoryRequest request, Integer id) {
        CategoryDTO categoryDTO = categoryRepository.findById(id).map(categoryMapper::toCategoryDTO).orElseThrow(
                () -> new ResourceNotFoundException(String.format(CATEGORY_ID_NOT_FOUND_ERROR, id)));
        categoryDTO.setName(request.getName());
        return categoryMapper.toCategoryDTO(categoryRepository.save(categoryMapper.toCategory(categoryDTO)));
    }

}
