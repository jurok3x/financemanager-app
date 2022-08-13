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
import com.financemanager.dto.IncomeDTO;
import com.financemanager.dto.RoleDTO;
import com.financemanager.dto.UserDTO;
import com.financemanager.entity.utils.DatePart;
import com.financemanager.service.IncomeService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebMvcTest(value = IncomeController.class,  useDefaultFilters = false, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = IncomeController.class)})
@AutoConfigureMockMvc(addFilters = false)
class IncomeControllerTest {
    
    @Autowired
    private MockMvc mvc;
    private ObjectMapper mapper;
    @MockBean
    private IncomeService incomeService;

    @Test
    void whenFindById_thenReturnStatus200() throws Exception {
        IncomeDTO incomeDTO = prepareIncomeDTO();
        given(incomeService.findById(Mockito.anyLong())).willReturn(incomeDTO);
        mvc.perform(get(String.format("/api/incomes/%d", incomeDTO.getId())))
        .andExpect(status().isOk());
        verify(incomeService).findById(incomeDTO.getId());
    }
    
    @Test
    void whenFindByUserIdAndCategoryIdAndDatePart_thenReturnStatus200() throws Exception {
        List<IncomeDTO> incomes = Arrays.asList(prepareIncomeDTO());
        given(incomeService.findByUserIdAndDatePart(Mockito.anyInt(), Mockito.any(DatePart.class)))
            .willReturn(incomes);
        mvc.perform(get(String.format("/api/incomes/user/%d", incomes.get(0).getUserDTO().getId()))
                .param("month", "11")
                .param("year", "2020"))
        .andExpect(status().isOk());
        verify(incomeService).findByUserIdAndDatePart(Mockito.anyInt(), Mockito.any(DatePart.class));
    }
    
    @Test
    void whenFindPageByUserIdAndCategoryIdAndDatePart_thenReturnStatus200() throws Exception {
        List<IncomeDTO> incomes = Arrays.asList(prepareIncomeDTO());
        given(incomeService.findByUserIdAndDatePart(Mockito.anyInt(), Mockito.any(DatePart.class),
                Mockito.any(Pageable.class))).willReturn(Page.empty());
        mvc.perform(get(String.format("/api/incomes/page/user/%d", incomes.get(0).getUserDTO().getId()))
                .param("month", "11")
                .param("year", "2020")
                .param("size", "5")
                .param("page", "2"))
        .andExpect(status().isOk());
        verify(incomeService).findByUserIdAndDatePart(Mockito.anyInt(), Mockito.any(DatePart.class),
                Mockito.any(Pageable.class));
    }
    
    @Test
    void whenSave_thenReturnStatus201() throws Exception {
        mapper = new ObjectMapper();
        IncomeDTO incomeDTO = prepareIncomeDTO();
        given(incomeService.save(Mockito.any(IncomeDTO.class))).willReturn(incomeDTO);
        mvc.perform(post("/api/incomes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(incomeDTO)))
        .andExpect(status().isCreated());
        verify(incomeService).save(Mockito.any(IncomeDTO.class));
    }
    
    @Test
    void whenUpdate_thenReturnStatus200() throws Exception {
        mapper = new ObjectMapper();
        IncomeDTO incomeDTO = prepareIncomeDTO();
        given(incomeService.update(Mockito.any(IncomeDTO.class), Mockito.anyLong())).willReturn(incomeDTO);
        mvc.perform(put(String.format("/api/incomes/%d", incomeDTO.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(incomeDTO)))
        .andExpect(status().isOk());
        verify(incomeService).update(incomeDTO, incomeDTO.getId());
    }
    
    @Test
    void whenDelete_thenReturnStatus204() throws Exception {
        IncomeDTO incomeDTO = prepareIncomeDTO();
        Mockito.doNothing().when(incomeService).delete(Mockito.anyLong());
        mvc.perform(delete(String.format("/api/incomes/%d", incomeDTO.getId())))
        .andExpect(status().isNoContent());
        verify(incomeService).delete(incomeDTO.getId());
    }
    
    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(incomeService);
    }
    
    private IncomeDTO prepareIncomeDTO() {
        IncomeDTO incomeDTO = new IncomeDTO();
        incomeDTO.setId(1L);
        incomeDTO.setAmount(220);
        LocalDate date = LocalDate.of(2022, 12, 21);
        incomeDTO.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        incomeDTO.setName("Salary");
        incomeDTO.setUserDTO(prepareUserDTO());
        return incomeDTO; 
    }
    
    private UserDTO prepareUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setEmail("jurok3x@gmail.com");
        userDTO.setName("Yurii");
        userDTO.setRoleDTO(new RoleDTO(2, "ROLE_USER"));
        return userDTO;
    }

}
