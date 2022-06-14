package com.financemanager.demo.site.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.financemanager.demo.site.dto.CategoryDTO;
import com.financemanager.demo.site.entity.payload.SaveCategoryRequest;
import com.financemanager.demo.site.entity.projects.ProjectCategoryAndCost;
import com.financemanager.demo.site.entity.projects.ProjectCategoryAndCount;
import com.financemanager.demo.site.exception.ResourceNotFoundException;
import com.financemanager.demo.site.mapper.CategoryMapper;
import com.financemanager.demo.site.repository.CategoryRepository;
import com.financemanager.demo.site.service.CategoryService;
import com.financemanager.demo.site.service.utils.ItemsUtils;

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
    public List<ProjectCategoryAndCost> getCategoriesAndCost(Optional<Integer> year, Optional<Integer> month) {
        return categoryRepository.getCategoriesAndCostByDate(ItemsUtils.getContextUser().getId(), ItemsUtils.formatDateString(year, month));
    }

    @Override
    public List<ProjectCategoryAndCount> getCategoriesAndCount(Optional<Integer> year, Optional<Integer> month) {
        return categoryRepository.getCategoriesAndCountByDate(ItemsUtils.getContextUser().getId(), ItemsUtils.formatDateString(year, month));
    }

    @Override
    public CategoryDTO update(SaveCategoryRequest request, Integer id) {
        CategoryDTO categoryDTO = categoryRepository.findById(id).map(categoryMapper::toCategoryDTO).orElseThrow(
                () -> new ResourceNotFoundException(String.format(CATEGORY_ID_NOT_FOUND_ERROR, id)));
        categoryDTO.setName(request.getName());
        return categoryMapper.toCategoryDTO(categoryRepository.save(categoryMapper.toCategory(categoryDTO)));
    }

}
