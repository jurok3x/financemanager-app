package com.financemanager.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.financemanager.dto.ExpenseDTO;
import com.financemanager.entity.CustomUserDetails;
import com.financemanager.entity.utils.DatePart;
import com.financemanager.exception.ResourceNotFoundException;
import com.financemanager.mapper.ExpensesMapper;
import com.financemanager.repository.ExpensesRepository;
import com.financemanager.service.ExpenseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@PropertySource(value = { "classpath:/messages/expense/info.properties" })
public class DefaultExpenseService implements ExpenseService {
    
    private final ExpensesRepository expensesRepository;
    private final ExpensesMapper expensesMapper;
    
    @Value("${expense_id_not_found.error}")
    private String expenseNotFoundError;

    @Override
    public ExpenseDTO save(ExpenseDTO expenseDTO) {
        return expensesMapper.toExpenseDTO(expensesRepository.save(expensesMapper.toExpense(expenseDTO)));
    }

    @Override
    public ExpenseDTO update(ExpenseDTO expenseDTO, Long id) {
        checkPermission(expenseDTO);
        expensesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(expenseNotFoundError, id)));
        return expensesMapper.toExpenseDTO(expensesRepository.save(expensesMapper.toExpense(expenseDTO)));
    }

    @Override
    public ExpenseDTO findById(Long id) {
        ExpenseDTO expenseDTO = expensesRepository.findById(id).map(expensesMapper::toExpenseDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(expenseNotFoundError, id)));
        checkPermission(expenseDTO);
        return expenseDTO;
    }

    @Override
    public void delete(Long id) {
        ExpenseDTO expenseDTO = expensesRepository.findById(id).map(expensesMapper::toExpenseDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(expenseNotFoundError, id)));
        checkPermission(expenseDTO);    
        expensesRepository.deleteById(expenseDTO.getId());
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
    
    private void checkPermission(ExpenseDTO expenseDTO){
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(expenseDTO.getUserDTO().getId() != user.getId() && !user.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
           throw new AccessDeniedException("You don't have permission for this action"); 
        }
    }

}