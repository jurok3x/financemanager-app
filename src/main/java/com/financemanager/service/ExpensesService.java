package com.financemanager.service;

import java.util.List;
import java.util.Optional;

import com.financemanager.dto.ExpenseDTO;
import com.financemanager.entity.utils.DatePart;

import org.springframework.stereotype.Component;

@Component
public interface ExpensesService {
	ExpenseDTO save(ExpenseDTO exspenseDTO);
	
	ExpenseDTO update(ExpenseDTO exspenseDTO, Long id);
	
	Optional<ExpenseDTO> findById(Long id);

    void delete(Long id);

    List<ExpenseDTO> findByUserIdAndCategoryIdAndDatePart(Integer userId, Integer categoryId, DatePart datePart);
    
}
