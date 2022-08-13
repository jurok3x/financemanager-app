package com.financemanager.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financemanager.dto.CategoryDTO;
import com.financemanager.service.CategoryService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebMvcTest(value = CategoryController.class, useDefaultFilters = false, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CategoryController.class) })
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;
    private ObjectMapper mapper;
    @MockBean
    private CategoryService categoryService;

    @Test
    void whenFindById_thenReturnStatus200() throws Exception {
        CategoryDTO categoryDTO = prepareCategoryDTO();
        given(categoryService.findById(Mockito.anyInt())).willReturn(categoryDTO);
        mvc.perform(get(String.format("/api/categories/%d", categoryDTO.getId())))
        .andExpect(status().isOk());
        verify(categoryService).findById(categoryDTO.getId());
    }

    @Test
    void whenFindAll_thenReturnStatus200() throws Exception {
        List<CategoryDTO> categories = Arrays.asList(prepareCategoryDTO());
        given(categoryService.findAll()).willReturn(categories);
        mvc.perform(get("/api/categories"))
        .andExpect(status().isOk());
        verify(categoryService).findAll();
    }

    @Test
    void whenFindByUserId_thenReturnStatus200() throws Exception {
        List<CategoryDTO> categories = Arrays.asList(prepareCategoryDTO());
        given(categoryService.findByUserId(Mockito.anyInt())).willReturn(categories);
        mvc.perform(get("/api/categories/user/1"))
        .andExpect(status().isOk());
        verify(categoryService).findByUserId(Mockito.anyInt());
    }
    
    @Test
    void whenSave_thenReturnStatus201() throws Exception {
        mapper = new ObjectMapper();
        CategoryDTO categoryDTO = prepareCategoryDTO();
        given(categoryService.save(Mockito.any(CategoryDTO.class))).willReturn(categoryDTO);
        mvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(categoryDTO)))
        .andExpect(status().isCreated());
        verify(categoryService).save(Mockito.any(CategoryDTO.class));
    }
    
    @Test
    void whenUpdate_thenReturnStatus200() throws Exception {
        mapper = new ObjectMapper();
        CategoryDTO categoryDTO = prepareCategoryDTO();
        given(categoryService.update(Mockito.any(CategoryDTO.class), Mockito.anyInt())).willReturn(categoryDTO);
        mvc.perform(put(String.format("/api/categories/%d", categoryDTO.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(categoryDTO)))
        .andExpect(status().isOk());
        verify(categoryService).update(categoryDTO, categoryDTO.getId());
    }
    
    @Test
    void whenDelete_thenReturnStatus204() throws Exception {
        CategoryDTO categoryDTO = prepareCategoryDTO();
        Mockito.doNothing().when(categoryService).delete(Mockito.anyInt());
        mvc.perform(delete(String.format("/api/categories/%d", categoryDTO.getId())))
        .andExpect(status().isNoContent());
        verify(categoryService).delete(categoryDTO.getId());
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(categoryService);
    }

    private CategoryDTO prepareCategoryDTO() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("Food");
        categoryDTO.setUsersId(Arrays.asList(1).stream().collect(Collectors.toSet()));
        return categoryDTO;
    }

}
