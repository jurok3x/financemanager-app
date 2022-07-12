package com.financemanager.mapper;

import com.financemanager.dto.ExpenseDTO;
import com.financemanager.entity.Expense;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ExpensesMapper {
    
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    
    public ExpenseDTO toExpenseDTO(Expense expense) {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setId(expense.getId());
        expenseDTO.setName(expense.getName());
        expenseDTO.setPrice(expense.getPrice());
        expenseDTO.setDate(expense.getDate());
        expenseDTO.setUser(userMapper.toUserDTO(expense.getUser()));
        expenseDTO.setCategory(categoryMapper.toCategoryDTO(expense.getCategory()));
        return expenseDTO;
    }
    
    public Expense toExpense(ExpenseDTO expenseDTO) {
        Expense expense = new Expense();
        expense.setId(expenseDTO.getId());
        expense.setName(expenseDTO.getName());
        expense.setPrice(expenseDTO.getPrice());
        expense.setDate(expenseDTO.getDate());
        expense.setUser(userMapper.toUser(expenseDTO.getUser()));
        expense.setCategory(categoryMapper.toCategory(expenseDTO.getCategory()));
        return expense;
    }

}
