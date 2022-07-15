package com.financemanager.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.financemanager.entity.Income;
import com.financemanager.entity.Role;
import com.financemanager.entity.User;
import com.financemanager.mapper.IncomeMapper;
import com.financemanager.mapper.UserMapper;
import com.financemanager.repository.IncomeRepository;
import com.financemanager.service.IncomeService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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
        
    }
    
    @Test
    void whenUpdate_thenReturnCorrectResult() {
        
    }
    
    @Test
    void whenFindById_thenReturnCorrectResult() {
        
    }
    
    @Test
    void whenFindAll_thenReturnCorrectResult() {
        
    }
    
    @Test
    void verifyDeleteById() {
        
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(incomeRepository);
    }
    
    private Income prepareIncome() {
        Income income = new Income();
        income.setAmount(220);
        LocalDate date = LocalDate.of(2022, 12, 21);
        income.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        income.setName("Salary");
        income.setUser(new User(null, "Yurii", "metro", "jurok3x@gmail.com", Role.ADMIN));
        return income; 
    }

}
