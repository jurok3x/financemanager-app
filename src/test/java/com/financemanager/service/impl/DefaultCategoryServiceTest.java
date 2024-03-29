package com.financemanager.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.financemanager.entity.Category;
import com.financemanager.entity.CustomUserDetails;
import com.financemanager.entity.Role;
import com.financemanager.entity.User;
import com.financemanager.mapper.CategoryMapper;
import com.financemanager.repository.CategoryRepository;
import com.financemanager.service.CategoryService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(value = { MockitoExtension.class })
class DefaultCategoryServiceTest {
    
    @Mock
    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;
    private CategoryService categoryService;
    @Mock
    private Authentication auth;
    
    @BeforeEach
    void setUp() {
        categoryMapper = new CategoryMapper();
        categoryService = new DefaultCategoryService(categoryRepository, categoryMapper);
    }
    
    @Test
    void whenSave_thenReturnCorrectResult() {
        Category category = prepareCategory();
        given(categoryRepository.save(Mockito.any(Category.class))).willReturn(category);
        assertEquals(categoryMapper.toCategoryDTO(category), categoryService.save(categoryMapper.toCategoryDTO(category)));
        verify(categoryRepository).save(Mockito.any(Category.class));
    }
    
    @Test
    void whenUpdate_thenReturnCorrectResult() {
        Category category = prepareCategory();
        prepareContext();
        given(categoryRepository.findById(Mockito.anyInt())).willReturn(Optional.of(category));
        given(categoryRepository.save(Mockito.any(Category.class))).willReturn(category);
        assertEquals(categoryMapper.toCategoryDTO(category), categoryService.update(categoryMapper.toCategoryDTO(category), category.getId()));
        verify(categoryRepository).findById(category.getId());
        verify(categoryRepository).save(Mockito.any(Category.class));
    }
    
    @Test
    void whenFindById_thenReturnCorrectResult() {
        Category category = prepareCategory();
        given(categoryRepository.findById(Mockito.anyInt())).willReturn(Optional.of(category));
        assertEquals(categoryMapper.toCategoryDTO(category), categoryService.findById(category.getId()));
        verify(categoryRepository).findById(category.getId());
    }
    
    @Test
    void whenFindAll_thenReturnCorrectResult() {
        List<Category> categories = Arrays.asList(prepareCategory());
        given(categoryRepository.findAll()).willReturn(categories);
        assertEquals(categories.stream().map(categoryMapper::toCategoryDTO).collect(Collectors.toList()), categoryService.findAll());
        verify(categoryRepository).findAll();
    }
    
    @Test
    void verifyDeleteById() {
        Category category = prepareCategory();
        given(categoryRepository.findById(Mockito.anyInt())).willReturn(Optional.of(category));
        prepareContext();
        categoryService.delete(category.getId());
        verify(categoryRepository).findById(category.getId());
        verify(categoryRepository).deleteById(category.getId());;
    }
    
    @Test
    void whenFindByUserId_thenReturnCorrectResult() {
        List<Category> categories = Arrays.asList(prepareCategory());
        given(categoryRepository.findByUserId(Mockito.anyInt())).willReturn(categories);
        assertEquals(categories.stream().map(categoryMapper::toCategoryDTO).collect(Collectors.toList()), categoryService.findByUserId(1));
        verify(categoryRepository).findByUserId(1);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(categoryRepository);
    }
    
    private Category prepareCategory() {
        Category category = new Category();
        category.setId(1);
        category.setName("Food");
        category.addUser(prepareUser());
        return category;
    }
    
    private void prepareContext() {
        CustomUserDetails user = CustomUserDetails.fromUserToCustomUserDetails(prepareUser());
        when(auth.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private User prepareUser() {
        return new User(1, "Yurii", "metro", "jurok3x@gmail.com", new Role(2, "ROLE_ADMIN"));
    }

}
