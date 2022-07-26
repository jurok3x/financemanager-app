package com.financemanager.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.financemanager.entity.Income;
import com.financemanager.entity.Role;
import com.financemanager.entity.User;
import com.financemanager.entity.utils.DatePart;
import com.financemanager.mapper.IncomeMapper;
import com.financemanager.mapper.UserMapper;
import com.financemanager.repository.IncomeRepository;
import com.financemanager.service.IncomeService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(value = { MockitoExtension.class })
class DefaultIncomeServiceTest {
    
    @Mock
    private IncomeRepository incomeRepository;
    private IncomeService incomeService;
    private IncomeMapper incomeMapper;
    
    @BeforeEach
    void setUp() {
        incomeMapper = new IncomeMapper(new UserMapper());
        incomeService = new DefaultIncomeService(incomeRepository, incomeMapper);
    }
    
    @Test
    void whenSave_thenReturnCorrectResult() {
        Income income = prepareIncome();
        given(incomeRepository.save(Mockito.any(Income.class))).willReturn(income);
        assertEquals(incomeMapper.toIncomeDTO(income), incomeService.save(incomeMapper.toIncomeDTO(income)));
        verify(incomeRepository).save(income);
    }
    
    @Test
    void whenUpdate_thenReturnCorrectResult() {
        Income income = prepareIncome();
        given(incomeRepository.save(Mockito.any(Income.class))).willReturn(income);
        given(incomeRepository.findById(Mockito.anyLong())).willReturn(Optional.of(income));
        assertEquals(incomeMapper.toIncomeDTO(income), incomeService.update(incomeMapper.toIncomeDTO(income), income.getId()));
        verify(incomeRepository).findById(income.getId());
        verify(incomeRepository).save(income);
    }
    
    @Test
    void whenFindById_thenReturnCorrectResult() {
        Income income = prepareIncome();
        given(incomeRepository.findById(Mockito.anyLong())).willReturn(Optional.of(income));
        assertEquals(incomeMapper.toIncomeDTO(income), incomeService.findById(income.getId()));
        verify(incomeRepository).findById(income.getId());
    }
    
    @Test
    void whenFindByUserIdAndDatePart_thenReturnCorrectResult() {
       List<Income> incomes = Arrays.asList(prepareIncome());
       given(incomeRepository.findByUserIdAndDatePart(Mockito.anyInt(), Mockito.any(DatePart.class))).willReturn(incomes);
       DatePart datePart = new DatePart(2022, 12);
       assertEquals(incomes.stream().map(incomeMapper::toIncomeDTO).collect(Collectors.toList()), incomeService.findByUserIdAndDatePart(1, datePart));
       verify(incomeRepository).findByUserIdAndDatePart(1, datePart);
    }
    
    @Test
    void whenFindByUserIdAndDatePartPage_thenReturnCorrectResult() {
        Page<Income> incomePage = Page.empty();
        given(incomeRepository.findByUserIdAndDatePart(Mockito.anyInt(), Mockito.any(DatePart.class), Mockito.any(Pageable.class))).willReturn(incomePage);
        DatePart datePart = new DatePart(2022, 12);
        assertEquals(incomePage, incomeService.findByUserIdAndDatePart(1, datePart, PageRequest.of(1, 5)));
        verify(incomeRepository).findByUserIdAndDatePart(1, datePart, PageRequest.of(1, 5));
    }
    
    @Test
    void verifyDeleteById() {
        Income income = prepareIncome();
        given(incomeRepository.findById(Mockito.anyLong())).willReturn(Optional.of(income));
        incomeService.delete(income.getId());
        verify(incomeRepository).findById(income.getId());
        verify(incomeRepository).deleteById(income.getId());
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(incomeRepository);
    }
    
    private Income prepareIncome() {
        Income income = new Income();
        income.setId(1L);
        income.setAmount(220);
        LocalDate date = LocalDate.of(2022, 12, 21);
        income.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        income.setName("Salary");
        income.setUser(new User(null, "Yurii", null, "jurok3x@gmail.com", new Role(2, "ROLE_USER")));
        return income; 
    }

}
