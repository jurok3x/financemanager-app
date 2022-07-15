package com.financemanager.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.financemanager.entity.Category;
import com.financemanager.entity.Expense;
import com.financemanager.entity.Role;
import com.financemanager.entity.User;
import com.financemanager.mapper.CategoryMapper;
import com.financemanager.mapper.ExpensesMapper;
import com.financemanager.mapper.UserMapper;
import com.financemanager.repository.ExpensesRepository;
import com.financemanager.service.ExpensesService;

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
class DefaultExpensesServiceTest {
    
    @Mock
    private ExpensesRepository expensesRepository;
    private ExpensesMapper expensesMapper;
    private ExpensesService expensesService;
    
    @BeforeEach
    void setUp() {
        expensesMapper = new ExpensesMapper(new UserMapper(), new CategoryMapper());
        expensesService = new DefaultExpensesService(expensesRepository, expensesMapper);
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
        verifyNoMoreInteractions(expensesRepository);
    }
    
    private Expense prepareExspense() {
        Expense expense = new Expense();
        expense.setName("Pizza");
        expense.setPrice(222);
        expense.setCategory(new Category());
        expense.setUser(new User(null, "Yurii", "metro", "jurok3x@gmail.com", Role.ADMIN));
        LocalDate date = LocalDate.of(2022, 12, 21);
        expense.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return expense;
    }

}
