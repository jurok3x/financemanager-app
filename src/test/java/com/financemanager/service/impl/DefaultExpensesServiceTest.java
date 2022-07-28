package com.financemanager.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.financemanager.entity.Category;
import com.financemanager.entity.Expense;
import com.financemanager.entity.Role;
import com.financemanager.entity.User;
import com.financemanager.entity.utils.DatePart;
import com.financemanager.mapper.CategoryMapper;
import com.financemanager.mapper.ExpensesMapper;
import com.financemanager.mapper.RoleMapper;
import com.financemanager.mapper.UserMapper;
import com.financemanager.repository.ExpensesRepository;
import com.financemanager.service.ExpenseService;

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
class DefaultExpensesServiceTest {
    
    @Mock
    private ExpensesRepository expensesRepository;
    private ExpensesMapper expensesMapper;
    private ExpenseService expensesService;
    
    @BeforeEach
    void setUp() {
        expensesMapper = new ExpensesMapper(new UserMapper(new RoleMapper()), new CategoryMapper());
        expensesService = new DefaultExpenseService(expensesRepository, expensesMapper);
    }
    
    @Test
    void whenSave_thenReturnCorrectResult() {
        Expense expense = prepareExspense();
        given(expensesRepository.save(Mockito.any(Expense.class))).willReturn(expense);
        assertEquals(expensesMapper.toExpenseDTO(expense), expensesService.save(expensesMapper.toExpenseDTO(expense)));
        verify(expensesRepository).save(Mockito.any(Expense.class));
    }
    
    @Test
    void whenUpdate_thenReturnCorrectResult() {
        Expense expense = prepareExspense();
        given(expensesRepository.save(Mockito.any(Expense.class))).willReturn(expense);
        given(expensesRepository.findById(Mockito.anyLong())).willReturn(Optional.of(expense));
        assertEquals(expensesMapper.toExpenseDTO(expense), expensesService.update(expensesMapper.toExpenseDTO(expense), expense.getId()));
        verify(expensesRepository).findById(expense.getId()); 
        verify(expensesRepository).save(Mockito.any(Expense.class)); 
    }
    
    @Test
    void whenFindById_thenReturnCorrectResult() {
        Expense expense = prepareExspense();
        given(expensesRepository.findById(Mockito.anyLong())).willReturn(Optional.of(expense));
        assertEquals(expensesMapper.toExpenseDTO(expense), expensesService.findById(expense.getId()));
        verify(expensesRepository).findById(expense.getId());
    }
    
    @Test
    void whenByUserIdAndCategoryIdAndDatePart_thenReturnCorrectResult() {
        List<Expense> expenses = Arrays.asList(prepareExspense());
        given(expensesRepository.findByUserIdAndCategoryIdAndDatePart(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(DatePart.class)))
                .willReturn(expenses);
        DatePart datePart = new DatePart(2022, 12);
        assertEquals(expenses.stream().map(expensesMapper::toExpenseDTO).collect(Collectors.toList()),
                expensesService.findByUserIdAndCategoryIdAndDatePart(1, 1, datePart));
        verify(expensesRepository).findByUserIdAndCategoryIdAndDatePart(1, 1, datePart);
    }
    
    @Test
    void whenByUserIdAndCategoryIdAndDatePartPage_thenReturnCorrectResult() {
        Page<Expense> expensesPage = Page.empty();
        given(expensesRepository.findByUserIdAndCategoryIdAndDatePart(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(DatePart.class), Mockito.any(Pageable.class)))
                .willReturn(expensesPage);
        DatePart datePart = new DatePart(2022, 12);
        assertEquals(expensesPage,
                expensesService.findByUserIdAndCategoryIdAndDatePart(1, 1, datePart, PageRequest.of(1, 5)));
        verify(expensesRepository).findByUserIdAndCategoryIdAndDatePart(1, 1, datePart, PageRequest.of(1, 5));
    }
    
    @Test
    void verifyDeleteById() {
        Expense expense = prepareExspense();
        given(expensesRepository.findById(Mockito.anyLong())).willReturn(Optional.of(expense));
        expensesService.delete(expense.getId());
        verify(expensesRepository).findById(expense.getId());
        verify(expensesRepository).deleteById(expense.getId());
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(expensesRepository);
    }
    
    private Expense prepareExspense() {
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setName("Pizza");
        expense.setPrice(222.0);
        LocalDate date = LocalDate.of(2022, 12, 21);
        expense.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Category category = new Category();
        category.setId(1);
        category.setName("Food");
        expense.setCategory(category);
        User user = new User(null, "Yurii", null, "jurok3x@gmail.com", new Role(2, "ROLE_USER"));
        user.setId(1);
        expense.setUser(user);
        return expense;
    }

}
