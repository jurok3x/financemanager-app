package com.financemanager.service;

import java.util.List;
import java.util.Optional;

import com.financemanager.dto.ExpenseDTO;
import com.financemanager.entity.payload.SaveExpenseRequest;
import com.financemanager.entity.utils.DatePart;

import org.springframework.stereotype.Component;

@Component
public interface ExpensesService {
	ExpenseDTO save(SaveExpenseRequest request);
	
	ExpenseDTO update(SaveExpenseRequest request, Long id);
	
	Optional<ExpenseDTO> findById(Long id);

    void delete(Long id);

    List<ExpenseDTO> findByUserId(Integer userId, Integer categoryId, DatePart datePart);
    
}
