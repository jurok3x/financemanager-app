package com.financemanager.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.financemanager.dto.ExpenseDTO;
import com.financemanager.entity.utils.DatePart;
import com.financemanager.exception.ResourceNotFoundException;
import com.financemanager.mapper.ExpensesMapper;
import com.financemanager.repository.ExpensesRepository;
import com.financemanager.service.ExpensesService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DefaultExpensesService implements ExpensesService {
    private static final String EXPENSE_ID_NOT_FOUND_ERROR = "Expense with id - %d, not found";
    private final ExpensesRepository expensesRepository;
    private final ExpensesMapper expensesMapper;

    @Override
    public ExpenseDTO save(ExpenseDTO expenseDTO) {
        return expensesMapper.toExpenseDTO(expensesRepository.save(expensesMapper.toExpense(expenseDTO)));
    }

    @Override
    public ExpenseDTO update(ExpenseDTO expenseDTO, Long id) {
        expensesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(EXPENSE_ID_NOT_FOUND_ERROR, id)));
        return expensesMapper.toExpenseDTO(expensesRepository.save(expensesMapper.toExpense(expenseDTO)));
    }

    @Override
    public Optional<ExpenseDTO> findById(Long id) {
        ExpenseDTO expenseDTO = expensesRepository.findById(id).map(expensesMapper::toExpenseDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(EXPENSE_ID_NOT_FOUND_ERROR, id)));
        return Optional.ofNullable(expenseDTO);
    }

    @Override
    public void delete(Long id) {
        ExpenseDTO deletedExpense = expensesRepository.findById(id).map(expensesMapper::toExpenseDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(EXPENSE_ID_NOT_FOUND_ERROR, id)));
        expensesRepository.deleteById(deletedExpense.getId());
    }

    @Override
    public List<ExpenseDTO> findByUserIdAndCategoryIdAndDatePart(Integer userId, Integer categoryId, DatePart datePart) {
        return expensesRepository
                .findByUserIdAndCategoryIdAndDatePart(userId, categoryId, datePart)
                .stream().map(expensesMapper::toExpenseDTO).collect(Collectors.toList());
    }

    @Override
    public Page<ExpenseDTO> findByUserIdAndCategoryIdAndDatePart(Integer userId, Integer categoryId, DatePart datePart,
            Pageable pageable) {
        return expensesRepository.findByUserIdAndCategoryIdAndDatePart(userId, categoryId, datePart, pageable).map(expensesMapper::toExpenseDTO);
    }

}