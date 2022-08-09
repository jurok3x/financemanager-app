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
import com.financemanager.dto.ExpenseDTO;
import com.financemanager.entity.utils.DatePart;
import com.financemanager.service.ExpenseService;
import com.financemanager.service.assembler.ExpenseModelAssembler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebMvcTest(value = ExpensesController.class,  useDefaultFilters = false, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ExpensesController.class)})
@AutoConfigureMockMvc(addFilters = false)
@Import(ExpenseModelAssembler.class)
class ExpensesControllerTest {
    
    @Autowired
    private MockMvc mvc;
    private ObjectMapper mapper;
    @MockBean
    private ExpenseService expenseService;
    
    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void whenFindById_thenReturnStatus200() throws Exception {
        ExpenseDTO expenseDTO = prepareExpenseDTO();
        given(expenseService.findById(Mockito.anyLong())).willReturn(expenseDTO);
        mvc.perform(get(String.format("/api/expenses/%d", expenseDTO.getId())))
        .andExpect(status().isOk());
        verify(expenseService).findById(expenseDTO.getId());
    }

    @Test
    void whenFindByUserIdAndCategoryIdAndDatePart_thenReturnStatus200() throws Exception {
        List<ExpenseDTO> expenses = Arrays.asList(prepareExpenseDTO());
        given(expenseService.findByUserIdAndCategoryIdAndDatePart(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(DatePart.class)))
            .willReturn(expenses);
        mvc.perform(get(String.format("/api/expenses/user/%d", expenses.get(0).getUserId()))
                .param("categoryId", "2")
                .param("month", "11")
                .param("year", "2020"))
        .andExpect(status().isOk());
        verify(expenseService).findByUserIdAndCategoryIdAndDatePart(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(DatePart.class));
    }
    
    @Test
    void whenFindPageByUserIdAndCategoryIdAndDatePart_thenReturnStatus200() throws Exception {
        List<ExpenseDTO> expenses = Arrays.asList(prepareExpenseDTO());
        given(expenseService.findByUserIdAndCategoryIdAndDatePart(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(DatePart.class),
                Mockito.any(Pageable.class))).willReturn(Page.empty());
        mvc.perform(get(String.format("/api/expenses/page/user/%d", expenses.get(0).getUserId()))
                .param("categoryId", "2")
                .param("month", "11")
                .param("year", "2020")
                .param("size", "5")
                .param("page", "2"))
        .andExpect(status().isOk());
        verify(expenseService).findByUserIdAndCategoryIdAndDatePart(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(DatePart.class),
                Mockito.any(Pageable.class));
    }
    
    @Test
    void whenSave_thenReturnStatus201() throws Exception {
        mapper = new ObjectMapper();
        ExpenseDTO expenseDTO = prepareExpenseDTO();
        given(expenseService.save(Mockito.any(ExpenseDTO.class))).willReturn(expenseDTO);
        mvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(expenseDTO)))
        .andExpect(status().isCreated());
        verify(expenseService).save(Mockito.any(ExpenseDTO.class));
    }
    
    @Test
    void whenUpdate_thenReturnStatus200() throws Exception {
        mapper = new ObjectMapper();
        ExpenseDTO expenseDTO = prepareExpenseDTO();
        given(expenseService.update(Mockito.any(ExpenseDTO.class), Mockito.anyLong())).willReturn(expenseDTO);
        mvc.perform(put(String.format("/api/expenses/%d", expenseDTO.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(expenseDTO)))
        .andExpect(status().isOk());
        verify(expenseService).update(expenseDTO, expenseDTO.getId());
    }
    
    @Test
    void whenDelete_thenReturnStatus204() throws Exception {
        ExpenseDTO expenseDTO = prepareExpenseDTO();
        Mockito.doNothing().when(expenseService).delete(Mockito.anyLong());
        mvc.perform(delete(String.format("/api/expenses/%d", expenseDTO.getId())))
        .andExpect(status().isNoContent());
        verify(expenseService).delete(expenseDTO.getId());
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(expenseService);
    }
    
    private ExpenseDTO prepareExpenseDTO() {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setId(1L);
        expenseDTO.setName("Pizza");
        expenseDTO.setPrice(222.0);
        LocalDate date = LocalDate.of(2022, 12, 21);
        expenseDTO.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        expenseDTO.setUserId(1);
        expenseDTO.setCategoryDTO(new CategoryDTO());
        return expenseDTO;
    }

}
